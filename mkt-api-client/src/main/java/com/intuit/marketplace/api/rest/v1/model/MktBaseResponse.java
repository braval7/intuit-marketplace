package com.intuit.marketplace.api.rest.v1.model;

import java.util.UUID;

/**
 * Base response that includes requestGuid
 *
 * @author Bhargav
 * @since 04/05/2018
 */
public class MktBaseResponse {

    private UUID requestGuid;
    private Long id;

    public MktBaseResponse() {
        super();
    }

    public MktBaseResponse(UUID requestGuid) {
        this.requestGuid = requestGuid;
    }

    public UUID getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(UUID requestGuid) {
        this.requestGuid = requestGuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
