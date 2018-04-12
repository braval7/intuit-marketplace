package com.intuit.marketplace.service.impl;

import com.google.common.annotations.VisibleForTesting;
import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;
import com.intuit.marketplace.api.rest.v1.response.MktProjectResponse;
import com.intuit.marketplace.data.domain.MktActor;
import com.intuit.marketplace.data.domain.MktProject;
import com.intuit.marketplace.data.domain.MktProjectBids;
import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.data.enums.MktProjectBidStatus;
import com.intuit.marketplace.data.repository.MktActorRepository;
import com.intuit.marketplace.data.repository.MktProjectBidsRepository;
import com.intuit.marketplace.data.repository.MktProjectRepository;
import com.intuit.marketplace.service.MktActorService;
import com.intuit.marketplace.service.MktProjectService;
import com.intuit.marketplace.service.exception.MktRuntimeException;
import com.intuit.marketplace.service.scheduler.MktAcceptProjectBidScheduler;
import com.intuit.marketplace.service.util.MktProjectHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Concrete implementation of MktProjectService
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Service
public class MktProjectServiceImpl implements MktProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktProjectServiceImpl.class);

    @Inject
    private MktProjectRepository mktProjectRepository;

    @Inject
    private MktActorRepository mktActorRepository;

    @Inject
    private MktProjectBidsRepository mktProjectBidsRepository;

    @Inject
    private MktActorService mktActorService;

    @Inject
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Inject
    private MktProjectHelper mktProjectHelper;

    @Override
    @Transactional
    public MktBaseResponse createProject(MktCreateProjectModel model) {
        LOGGER.info("executing createProject");
        // do data validations
        performValidations(model);

        // check for idempotency
        mktProjectHelper.checkForIdempotency(model.getRequestGuid());

        // perform lookup to see if actor exists
        Optional<MktActor> seller = mktActorRepository.findById(model.getSellerId());
        if (seller != null && !seller.isPresent()) {
            throw new MktRuntimeException("Seller doesn't exists, project creation failed");
        }

        // check if actor type is SELLER
        if (seller.isPresent() && seller.get().getActorType() != MktActorType.SELLER) {
            throw new MktRuntimeException("Given actor is not a project seller, project creation failed");
        }

        // check that last date for bid is not in the past
        if (model.getLastDayForBids().withZone(DateTimeZone.UTC)
                .isBefore(DateTime.now(DateTimeZone.UTC))) {
            throw new MktRuntimeException("Project's last day for bids can't be in the past, project creation failed");
        }

        // convert to MktProject object
        MktProject project = new MktProject();
        project.setMaximumBudget(model.getMaximumBudget());
        project.setLastDayForBids(model.getLastDayForBids());
        project.setSeller(seller.get());
        project.setDescription(model.getDescription());

        // save the object to database
        project = mktProjectRepository.save(project);

        // create the scheduler to accept the bids for this project
        LOGGER.info("Creating scheduler to accept the lowest bid on the project {}", project.getId());
        threadPoolTaskScheduler.schedule(new MktAcceptProjectBidScheduler(project, mktProjectBidsRepository), project.getLastDayForBids().toDate());

        MktBaseResponse response = new MktBaseResponse();
        response.setRequestGuid(model.getRequestGuid());
        response.setId(project.getId());

        LOGGER.info("Project object saved to database");

        return response;
    }

    @VisibleForTesting
    void performValidations(MktCreateProjectModel model) {
        // validate if input is null
        if (model == null) {
            throw new MktRuntimeException("Provided model is null, Project creation failed");
        }

        // check for required fields
        if (model.getMaximumBudget() == null) {
            throw new MktRuntimeException("Maximum Budget for a project can't be null, Project creation failed");
        }

        if (model.getLastDayForBids() == null) {
            throw new MktRuntimeException("Last date for bids on project can't be null, Project creation failed");
        }

        if (model.getSellerId() == null) {
            throw new MktRuntimeException("Seller Id for a project can't be null, Project creation failed");
        }
    }

    @Override
    @Transactional
    public MktProjectResponse getProject(Long projectId) {
        LOGGER.info("executing getProject");

        // check if project exists in the database, if not then throw error
        Optional<MktProject> project = mktProjectRepository.findById(projectId);

        if (!project.isPresent()) {
            throw new MktRuntimeException("project doesn't exists");
        }

        // find project buyers (bids)
        List<MktProjectBids> projectBidsList = mktProjectBidsRepository.
                findProjectBidsOrderByBidPrice(project.get().getId());

        // get seller
        MktActorResponse seller = mktActorService.getActor(project.get().getSeller().getId());

        // create response object
        MktProjectResponse response = new MktProjectResponse();

        // if project bids are not found then log a warning
        if (projectBidsList != null && !projectBidsList.isEmpty()) {
            // get first bid, which will be lowest
            BigDecimal lowestBid = projectBidsList.get(0).getBidPrice();
            response.setLowestBid(lowestBid);
        } else {
            LOGGER.warn("No Bids were made on this project {}", project.get().getId());
            response.setLowestBid(BigDecimal.ZERO);
        }

        // get list of buyers with bids on this project
        List<MktActorResponse> buyers = new ArrayList<>();

        // iterate over project bids
        for(MktProjectBids bids : projectBidsList) {
            // get Actor Response for given actor
            MktActorResponse buyerHavingBidOnThisProject = mktActorService.getActor(bids.getActor().getId());
            buyerHavingBidOnThisProject.setBidAmount(bids.getBidPrice());

            // add object to the list
            buyers.add(buyerHavingBidOnThisProject);
        }

        response.setId(project.get().getId());
        response.setDescription(project.get().getDescription());
        response.setLastDayForBids(project.get().getLastDayForBids());
        response.setProjectSeller(seller);
        response.setProjectBuyers(buyers);

        // return response
        return response;
    }

    @Override
    @Transactional
    public MktBaseResponse postProjectBid(Long projectId, Long buyerId, MktCreateProjectBidModel model) {
        LOGGER.info("executing postProjectBid");

        // do data validation
        if (projectId == null || buyerId == null || model == null) {
            throw new MktRuntimeException("ProjectId/buyerId or model is null, can't post bid");
        }

        // check for idempotency
        mktProjectHelper.checkForIdempotency(model.getRequestGuid());

        // get project entity and validate if it exists
        Optional<MktProject> project = mktProjectRepository.findById(projectId);
        if (project != null && !project.isPresent()) {
            throw new MktRuntimeException("project doesn't exists, can't post bid");
        }

        // get actor entity
        Optional<MktActor> buyer = mktActorRepository.findById(buyerId);
        if (buyer != null && !buyer.isPresent()) {
            throw new MktRuntimeException("buyer doesn't exists, can't post bid");
        }

        // check the type of the buyer
        if (buyer.get().getActorType() != MktActorType.BUYER) {
            throw new MktRuntimeException("Provided actor is not Buyer, can't post bid");
        }

        // check that bidPrice is lesser than maximum budget of project
        if (model.getBidPrice().compareTo(project.get().getMaximumBudget()) > 0) {
            throw new MktRuntimeException("Bid price is higher than maximum budget for a project, can't post bid");
        }

        // check if there exists a Bid for given buyer for given project
        // if it does then throw error
        MktProjectBids projectBids = mktProjectBidsRepository.findProjectBidsByProjectAndActor(project.get().getId(),
                buyer.get().getId());

        if (projectBids != null) {
            throw new MktRuntimeException("Actor " + buyer.get().getId() + " has already placed a bid on this project");
        }

        // passed data validation, go ahead and create record
        projectBids = new MktProjectBids();
        projectBids.setActor(buyer.get());
        projectBids.setProject(project.get());
        projectBids.setBidStatus(MktProjectBidStatus.PENDING);
        projectBids.setBidPrice(model.getBidPrice());

        projectBids = mktProjectBidsRepository.save(projectBids);

        MktBaseResponse response = new MktBaseResponse();
        response.setRequestGuid(model.getRequestGuid());
        response.setId(projectBids.getId());

        LOGGER.info("Bid posted successfully");

        return response;
    }

    @Override
    @Transactional
    public MktBaseResponse acceptProjectBid(Long projectId, Long buyerId) {
        LOGGER.info("executing acceptBid");

        // do data validation
        if (projectId == null || buyerId == null) {
            throw new MktRuntimeException("ProjectId/buyerId is null, can't accept bid");
        }

        // TODO check for idempotency through requestGuid,
        // though this is covered through unique constraint on actor, project

        // get project entity and validate if it exists
        Optional<MktProject> project = mktProjectRepository.findById(projectId);
        if (project != null && !project.isPresent()) {
            throw new MktRuntimeException("project doesn't exists, can't accept bid");
        }

        // get actor entity
        Optional<MktActor> buyer = mktActorRepository.findById(buyerId);
        if (buyer != null && !buyer.isPresent()) {
            throw new MktRuntimeException("buyer doesn't exists, can't accept bid");
        }

        // check if there exists a Bid for given buyer for given project
        // if it does NOT then throw error
        MktProjectBids projectBids = mktProjectBidsRepository.findProjectBidsByProjectAndActor(project.get().getId(),
                buyer.get().getId());

        if (projectBids == null) {
            throw new MktRuntimeException("Buyer " + buyer.get().getId() + " has not placed a bid on this project " + project.get().getId());
        }

        // passed data validation, update the bid status
        projectBids.setBidStatus(MktProjectBidStatus.ACCEPTED);
        projectBids = mktProjectBidsRepository.save(projectBids);

        LOGGER.info("Bid accepted successfully");

        MktBaseResponse response = new MktBaseResponse();
        response.setId(projectBids.getId());

        return response;
    }
}
