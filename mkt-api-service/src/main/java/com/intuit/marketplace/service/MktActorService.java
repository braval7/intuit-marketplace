package com.intuit.marketplace.service;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;

/**
 * Service interface for actor APIs
 *
 * @author Bhargav
 * @since 04/09/2018
 */
public interface MktActorService {

    /**
     * API that creates actor for given input
     * @param model
     */
    MktBaseResponse createActor(MktCreateActorModel model);

    /**
     * API that looks up if given actor exists, and
     * returns necessary data for that actor
     * @param actorId
     * @return
     */
    MktActorResponse getActor(Long actorId);
}
