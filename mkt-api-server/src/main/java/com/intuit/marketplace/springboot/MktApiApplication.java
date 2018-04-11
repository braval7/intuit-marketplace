package com.intuit.marketplace.springboot;

import com.intuit.marketplace.config.MktApplicationConfig;
import com.intuit.marketplace.config.MktSwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.ws.rs.ApplicationPath;
import java.util.HashMap;
import java.util.Map;

/**
 * Marketplace application for assessment
 *
 * @author Bhargav
 * @since 04/04/2018
 */
@Configuration
@SpringBootApplication
@ComponentScan("com.intuit.marketplace.service.config")
@EntityScan("com.intuit.marketplace.data.domain")
@Import({MktApplicationConfig.class})
@EnableAutoConfiguration
public class MktApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MktApiApplication.class, args);
    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        Map<String, String> initParams = new HashMap<>();
//        initParams.put("javax.ws.rs.Application", RestEasyConfig.class.getCanonicalName());
//
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new FilterDispatcher());
//        registrationBean.setInitParameters(initParams);
//        return registrationBean;
//    }

//    @Bean
//    public ServletContextInitializer initializer() {
//        return servletContext -> {
//            // RestEasy configuration
//            servletContext.setInitParameter("resteasy.scan", "true");
//            servletContext.setInitParameter("resteasy.servlet.mapping.prefix", "/services");
//        };
//    }

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        servletContext.setInitParameter("com.sun.faces.expressionFactory", "org.apache.el.ExpressionFactoryImpl");
//    }
}
