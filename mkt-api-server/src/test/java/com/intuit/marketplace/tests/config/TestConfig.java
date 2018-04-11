package com.intuit.marketplace.tests.config;

import com.intuit.marketplace.config.MktApplicationConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by braval on 4/10/18.
 */
@Configuration
@ComponentScan("com.intuit.marketplace.service.config")
@EntityScan("com.intuit.marketplace.data.domain")
@Import({MktApplicationConfig.class})
@EnableAutoConfiguration
public class TestConfig {

}
