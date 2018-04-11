package com.intuit.marketplace.data.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * DataSource configuration
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Configuration
//@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.intuit.**.data.repository")
public class MktDataConfig {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        DataSource ds = DataSourceBuilder.create()
                .username("sa")
                .password("sa")
                .url("jdbc:h2:file:~/marketplace")
                .driverClassName("org.h2.Driver")
                .build();

        return ds;
//        return DataSourceBuilder.create()
//                .username(environment.getRequiredProperty("spring.datasource.username"))
//                .password(environment.getRequiredProperty("spring.datasource.password"))
//                .url(environment.getRequiredProperty("spring.datasource.url"))
//                .driverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"))
//                .build();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
