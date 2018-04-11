package com.intuit.marketplace.api.rest.v1.response;

import java.util.List;

/**
 * List of existing actors
 *
 * @author Bhargav
 * @since 04/09/2018
 */
public class MktActorListResponse {

    private List<MktActorResponse> listOfActors;

    public List<MktActorResponse> getListOfActors() {
        return listOfActors;
    }

    public void setListOfActors(List<MktActorResponse> listOfActors) {
        this.listOfActors = listOfActors;
    }
}
