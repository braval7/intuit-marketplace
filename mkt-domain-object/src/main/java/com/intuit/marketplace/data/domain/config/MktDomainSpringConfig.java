package com.intuit.marketplace.data.domain.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

/**
 * Domain specific config
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Configuration
@Import({MktDataConfig.class})
@EnableJpaRepositories(basePackages = "com.intuit.marketplace.data.repository")
public class MktDomainSpringConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktDomainSpringConfig.class);

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("PostConstruct MktDomainSpringConfig completed.");
    }

}
