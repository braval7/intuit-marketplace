package com.intuit.marketplace.service;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktProjectResponse;

/**
 * Service interface for Project APIs
 *
 * @author Bhargav
 * @since 04/05/2018
 */
public interface MktProjectService {

    /**
     * API that creates Project for given input
     * @param model
     */
    MktBaseResponse createProject(MktCreateProjectModel model);

    /**
     * API that looks up if given project exists, and
     * returns necessary data for that project
     * @param projectId
     * @return
     */
    MktProjectResponse getProject(Long projectId);

    /**
     * Post a bid on a project
     *
     * @param projectId
     * @param buyerId
     * @param model
     * @return
     */
    MktBaseResponse postProjectBid(Long projectId, Long buyerId, MktCreateProjectBidModel model);

    /**
     * Accept the bid by given buyer on given project
     *
     * @param projectId
     * @param buyerId
     * @return
     */
    MktBaseResponse acceptProjectBid(Long projectId, Long buyerId);
}
