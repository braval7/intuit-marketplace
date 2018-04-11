package com.intuit.marketplace.api.rest.v1.model;

import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Model/Dto used to create a actor
 *
 * @author Bhargav
 * @since 04/09/2018
 */
public class MktCreateActorModel {

    @NotNull(message = "Request Guid is required")
    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.REQUEST_GUID_STRING)
    private UUID requestGuid;

    @NotNull(message = "First Name is required")
    @ApiModelProperty(required = true, dataType = "First Name")
    private String firstName;

    @NotNull(message = "Last Name is required")
    @ApiModelProperty(required = true, dataType = "Last Name")
    private String lastName;

    @NotNull(message = "Email is required")
    @ApiModelProperty(required = true, dataType = "bhargav@intuit.com")
    private String email;

    @NotNull(message = "Actor Type is required")
    @ApiModelProperty(required = true, dataType = "BUYER | SELLER")
    private String actorType;

    public @NotNull(message = "Request Guid is required") UUID getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(@NotNull(message = "Request Guid is required") UUID requestGuid) {
        this.requestGuid = requestGuid;
    }

    public @NotNull(message = "First Name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull(message = "First Name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotNull(message = "Last Name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull(message = "Last Name is required") String lastName) {
        this.lastName = lastName;
    }

    public @NotNull(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotNull(message = "Actor Type is required") String getActorType() {
        return actorType;
    }

    public void setActorType(@NotNull(message = "Actor Type is required") String actorType) {
        this.actorType = actorType;
    }
}


