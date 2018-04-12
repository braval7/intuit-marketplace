package com.intuit.marketplace.api.rest.v1;

import com.intuit.marketplace.api.rest.v1.model.MktAcceptProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktProjectResponse;
import com.intuit.marketplace.data.domain.MktProject;
import com.intuit.marketplace.service.MktProjectService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

/**
 * Concrete implementation of MktProjectResource
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Named
public class MktProjectResourceImpl implements MktProjectResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktProjectResourceImpl.class);

    @Inject
    private MktProjectService mktProjectService;

    @Override
    public Response createProject(MktCreateProjectModel model) {
        LOGGER.info("Creating Project with requestGuid {}", model.getRequestGuid());

        // call the service
        MktBaseResponse response = mktProjectService.createProject(model);

        // return response
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response postProjectBid(Long projectId, Long buyerId, MktCreateProjectBidModel model) {
        LOGGER.info("Posting a bid for projetId {} for buyerId {}", projectId, buyerId);

        // call the service
        MktBaseResponse response = mktProjectService.postProjectBid(projectId, buyerId, model);

        // return response
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response getAllProjects() {
        MktProject prj = new MktProject();
        prj.setId(1L);
        prj.setDescription("Dummy value");
        prj.setMaximumBudget(BigDecimal.TEN);
        prj.setLastDayForBids(DateTime.now());

        return Response.status(Response.Status.OK).entity(prj).build();
    }

    @Override
    public Response getProject(Long projectId) {
        // get data from service
        MktProjectResponse response = mktProjectService.getProject(projectId);

        // construct Response object and return
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response acceptProjectBid(Long projectId,Long buyerId, MktAcceptProjectBidModel model) {
        LOGGER.info("Accepting a bid for projetId {} for buyerId {}", projectId, buyerId);

        // call the service
        MktBaseResponse response = mktProjectService.acceptProjectBid(projectId, buyerId, model);
        // return response
        return Response.status(Response.Status.OK).entity(response).build();
    }

}
