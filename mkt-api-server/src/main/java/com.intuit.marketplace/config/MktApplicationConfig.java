package com.intuit.marketplace.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.intuit.marketplace.data.domain.config.MktDomainSpringConfig;
import com.intuit.marketplace.service.config.MktJerseyConfig;
import com.intuit.marketplace.service.config.MktProjectServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Application config
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Configuration
@ComponentScan(basePackages = { "com.intuit.marketplace.api.rest.*", "com.intuit.**.data.repository"})
@EnableTransactionManagement
@Import({
        MktDomainSpringConfig.class,
        MktProjectServiceConfig.class
})
@EnableScheduling
@EnableAsync
public class MktApplicationConfig {

        @Bean
        public ObjectMapper jsonObjectMapper() {
                ObjectMapper om = new ObjectMapper();
                om.registerModule(new JodaModule());
                return om;
        }
}
