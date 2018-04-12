package com.intuit.marketplace.api.rest.v1;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktProjectResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
@Path("api/v1/projects")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Api(value = "api/v1/projects", description = "Projects endpoints")
public interface MktProjectResource {

    @POST
    @Path("/")
    @PermitAll
    @ApiOperation(value = "Create a Project", response = MktBaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Project Created", response = MktBaseResponse.class),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response createProject(@Valid MktCreateProjectModel createProjectModel);

    @POST
    @Path("/{projectId}/post-bid/{buyerId}")
    @PermitAll
    @ApiOperation(value = "Post a bid for given project", response = MktBaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bid posted successful", response = MktBaseResponse.class),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response postProjectBid(@PathParam("projectId") Long projectId, @PathParam("buyerId") Long buyerId,
                     @Valid MktCreateProjectBidModel model);

    @GET
    @Path("/{projectId}")
    @PermitAll
    @ApiOperation(value = "Get single Project", response = MktProjectResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MktProjectResponse.class),
            @ApiResponse(code = 400, message = "Project does not exists"),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response getProject(@PathParam("projectId") Long projectId);

    @POST
    @Path("/{projectId}/accept-bid/{buyerId}")
    @PermitAll
    @ApiOperation(value = "Accept bid on given project by given buyer id", response = MktBaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bid accepted successfully", response = MktBaseResponse.class),
            @ApiResponse(code = 400, message = "Project does not exists"),
            @ApiResponse(code = 400, message = "Buyer does not exists"),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response acceptProjectBid(@PathParam("projectId") Long projectId, @PathParam("buyerId") Long buyerId);

    @GET
    @Path("/")
    @PermitAll
    @ApiOperation(value = "Get all Projects", response = MktProjectResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MktProjectResponse.class),
            @ApiResponse(code = 500, message = "Unexpected failure") })
    Response getAllProjects();
}
