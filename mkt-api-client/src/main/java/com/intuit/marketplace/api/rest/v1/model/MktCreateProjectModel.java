package com.intuit.marketplace.api.rest.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Model/Dto used to create a project
 *
 * @author Bhargav
 * @since 04/05/2018
 */
public class MktCreateProjectModel {

    @NotNull(message = "Request Guid is required")
    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.REQUEST_GUID_STRING)
    private UUID requestGuid;

    @NotNull(message = "Seller Id is required. This is the ID of actor with type Seller")
    @ApiModelProperty(required = true, dataType = "")
    private Long sellerId;

    @NotNull(message = "Maximum Budget is required")
    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.MONEY_VALUE_STRING)
    @JsonProperty
    @JsonSerialize
    private BigDecimal maximumBudget;

    @NotNull(message = "Last Day for Bids is required")
    @ApiModelProperty(required = true, dataType = MktSwaggerModelConstants.DATE_VALUE_STRING)
    private DateTime lastDayForBids;

    @ApiModelProperty(required = false)
    private String description;

    public @NotNull(message = "Request Guid is required") UUID getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(@NotNull(message = "Request Guid is required") UUID requestGuid) {
        this.requestGuid = requestGuid;
    }

    public @NotNull(message = "Maximum Budget is required") BigDecimal getMaximumBudget() {
        return maximumBudget;
    }

    public void setMaximumBudget(@NotNull(message = "Maximum Budget is required") BigDecimal maximumBudget) {
        this.maximumBudget = maximumBudget;
    }

    public @NotNull(message = "Last Day for Bids is required") DateTime getLastDayForBids() {
        return lastDayForBids;
    }

    public void setLastDayForBids(@NotNull(message = "Last Day for Bids is required") DateTime lastDayForBids) {
        this.lastDayForBids = lastDayForBids;
    }

    public @NotNull(message = "Seller Id is required. This is the ID of actor with type Seller") Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(@NotNull(message = "Seller Id is required. This is the ID of actor with type Seller") Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
