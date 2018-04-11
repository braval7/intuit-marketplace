package com.intuit.marketplace.api.rest.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Model to post bid on a project
 *
 * @author Bhargav
 * @since 04/10/2018
 */
public class MktCreateProjectBidModel {

    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.REQUEST_GUID_STRING)
    private @NotNull(message = "Request Guid is required")
    UUID requestGuid;

    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.MONEY_VALUE_STRING)
    @JsonProperty
    @JsonSerialize
    private @NotNull(message = "Bid Price is required")
    BigDecimal bidPrice;

    public @NotNull(message = "Request Guid is required") UUID getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(@NotNull(message = "Request Guid is required") UUID requestGuid) {
        this.requestGuid = requestGuid;
    }

    public @NotNull(message = "Bid Price is required") BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(@NotNull(message = "Bid Price is required") BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }
}
