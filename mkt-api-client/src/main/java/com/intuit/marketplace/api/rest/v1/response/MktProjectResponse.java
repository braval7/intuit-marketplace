package com.intuit.marketplace.api.rest.v1.response;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response model for getting a project API
 *
 * @author Bhargav
 * @since 04/09/2018
 */
public class MktProjectResponse extends MktBaseResponse {
    private String description;
    private BigDecimal lowestBid;
    private DateTime lastDayForBids;
    private MktActorResponse projectSeller;
    private List<MktActorResponse> projectBuyers;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLowestBid() {
        return lowestBid;
    }

    public void setLowestBid(BigDecimal lowestBid) {
        this.lowestBid = lowestBid;
    }

    public DateTime getLastDayForBids() {
        return lastDayForBids;
    }

    public void setLastDayForBids(DateTime lastDayForBids) {
        this.lastDayForBids = lastDayForBids;
    }

    public MktActorResponse getProjectSeller() {
        return projectSeller;
    }

    public void setProjectSeller(MktActorResponse projectSeller) {
        this.projectSeller = projectSeller;
    }

    public List<MktActorResponse> getProjectBuyers() {
        return projectBuyers;
    }

    public void setProjectBuyers(List<MktActorResponse> projectBuyers) {
        this.projectBuyers = projectBuyers;
    }
}
