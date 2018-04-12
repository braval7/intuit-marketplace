package com.intuit.marketplace.api.rest.v1;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorListResponse;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API endpoinds exposed on Project
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Named
@Path("api/v1/actors")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Api(value = "api/v1/actors", description = "Actor endpoints")
public interface MktActorResource {

    @POST
    @Path("/")
    @PermitAll
    @ApiOperation(value = "Create an Actor", response = MktBaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actor Created", response = MktBaseResponse.class),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response createActor(@Valid MktCreateActorModel createActorModel);

    @GET
    @Path("/{actorId}")
    @PermitAll
    @ApiOperation(value = "Get single actor for given actorId", response = MktActorResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MktActorResponse.class),
            @ApiResponse(code = 400, message = "Actor does not exists"),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response getActor(@PathParam("actorId") Long actorId);

}
