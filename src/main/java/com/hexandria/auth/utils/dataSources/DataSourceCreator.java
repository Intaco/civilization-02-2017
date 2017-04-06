package com.hexandria.auth.utils.dataSources;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;


/**
 * Created by root on 01.04.17.
 */
@Configuration
public class DataSourceCreator {

    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource(){
        return DataSourceBuilder.
                create()
                .password(environment.getProperty("db.password"))
                .username(environment.getProperty("db.user"))
                .driverClassName(environment.getProperty("db.driver"))
                .url(environment.getProperty("db.url"))
                .build();
    }
}