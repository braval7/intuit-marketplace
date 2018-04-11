package com.intuit.marketplace.api.rest.v1;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;
import com.intuit.marketplace.service.MktActorService;
import com.intuit.marketplace.service.MktProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Concrete implementation of MktActorResource
 *
 * @author Bhargav
 * @since 04/09/2018
 */
@Named
public class MktActorResourceImpl implements MktActorResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktActorResourceImpl.class);

    @Inject
    private MktActorService mktActorService;

    @Override
    public Response createActor(MktCreateActorModel model) {
        LOGGER.info("Creating actor with requestGuid {}", model.getRequestGuid());

        // call the service
        MktBaseResponse response = mktActorService.createActor(model);

        // return response
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response getActor(Long actorId) {
        LOGGER.info("Getting actor with id {}", actorId);

        // call the service
        MktActorResponse response = mktActorService.getActor(actorId);

        return Response.status(Response.Status.OK).entity(response).build();
    }
}
