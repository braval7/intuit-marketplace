package com.intuit.marketplace.service.config;

import com.intuit.marketplace.api.rest.v1.MktProjectResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Jersey Config
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Component
public class MktJerseyConfig extends ResourceConfig {

    public MktJerseyConfig() {
        packages("com.intuit.marketplace.api.rest.v1");
        register(MktProjectResource.class);
    }
}
