package com.intuit.marketplace.api.rest.v1.response;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;

import java.math.BigDecimal;

/**
 * Actor details for response
 *
 * @author Bhargav
 * @since 04/09/2018
 */
public class MktActorResponse extends MktBaseResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String actorType;
    private BigDecimal bidAmount;

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }
}
