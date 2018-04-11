package com.intuit.marketplace.service.config;

import com.intuit.marketplace.service.MktActorService;
import com.intuit.marketplace.service.MktProjectService;
import com.intuit.marketplace.service.impl.MktActorServiceImpl;
import com.intuit.marketplace.service.impl.MktProjectServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Define all the beans for the Project
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Configuration
public class MktProjectServiceConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MktProjectService mktProjectService() {
        return new MktProjectServiceImpl();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MktActorService mktActorService() {
        return new MktActorServiceImpl();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

}
