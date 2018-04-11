package com.intuit.marketplace.service.scheduler;

import com.intuit.marketplace.data.domain.MktProject;
import com.intuit.marketplace.data.domain.MktProjectBids;
import com.intuit.marketplace.data.enums.MktProjectBidStatus;
import com.intuit.marketplace.data.repository.MktProjectBidsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Predicate;

/**
 * Scheduler to accept the bid on the last day of the bid for given project
 *
 * @author Bhargav
 * @since 04/10/2018
 */
public class MktAcceptProjectBidScheduler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktAcceptProjectBidScheduler.class);

    private MktProject project;
    private MktProjectBidsRepository mktProjectBidsRepository;

    public MktAcceptProjectBidScheduler(MktProject project, MktProjectBidsRepository mktProjectBidsRepository) {
        this.project = project;
        this.mktProjectBidsRepository = mktProjectBidsRepository;
    }

    @Override
    @Transactional
    public void run() {
        LOGGER.info("Finding all bids for this project");

        // get all the bids for this project
        List<MktProjectBids> allBidsForThisProject = mktProjectBidsRepository.
                findProjectBidsOrderByBidPrice(this.project.getId());

        // if no bids are found then log a warning
        if (allBidsForThisProject != null && !allBidsForThisProject.isEmpty()) {
            // check if there is any ACCEPTED Bids for this project
            // if Bid is already ACCEPTED then we don't want to do anything
            Predicate<MktProjectBids> acceptedBids = bids -> bids.getBidStatus() == MktProjectBidStatus.ACCEPTED;
            Boolean isBidAccepted = allBidsForThisProject.stream().anyMatch(acceptedBids);
            // if bid is not accepted then accept the bid for the lowest amount
            if (!isBidAccepted) {
                // get the first object from list as this is sorted by the price and one to be accepted
                MktProjectBids projectBids = allBidsForThisProject.get(0);
                LOGGER.info("Accepting bids for projectId {} and buyerId {} ", projectBids.getProject().getId(),
                        projectBids.getActor().getId());

                projectBids.setBidStatus(MktProjectBidStatus.ACCEPTED);
                projectBids = mktProjectBidsRepository.save(projectBids);

                LOGGER.info("Accepted bid for projectId {}", projectBids.getProject().getId());
            } else {
                LOGGER.info("This project has already ACCEPTED bid, nothing to do!!");
            }
        } else {
            LOGGER.warn("No Bids were received for this project");
        }
    }
}
