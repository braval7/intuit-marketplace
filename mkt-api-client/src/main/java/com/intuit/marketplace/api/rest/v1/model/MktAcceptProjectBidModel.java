package com.intuit.marketplace.api.rest.v1.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Accept project bid
 *
 * @author Bhargav
 * @since 04/12/2018
 */
public class MktAcceptProjectBidModel {

    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.REQUEST_GUID_STRING)
    private @NotNull(message = "Request Guid is required")
    UUID requestGuid;

    public @NotNull(message = "Request Guid is required") UUID getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(@NotNull(message = "Request Guid is required") UUID requestGuid) {
        this.requestGuid = requestGuid;
    }
}
