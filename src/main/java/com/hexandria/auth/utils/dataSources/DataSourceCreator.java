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

    @Bean
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(){

        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getProperty("hibernate.showsql"));
        properties.put("hibernate.hbm2dll.auto", environment.getProperty("hibernate.hbm2dll.auto"));
        properties.put("hibernate.c3p0.min_size", "5");
        properties.put("hibernate.c3p0.max_size", "20");
        properties.put("hibernate.c3p0.timeout", "500");
        properties.put("hibernate.c3p0.max_statements", "50");
        properties.put("hibernate.c3p0.idle_test_period", "2000");


        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.hexandria.auth.common.user");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(properties);
        factoryBean.setPersistenceUnitName("users");

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userEntityManagerFactory().getObject());
        return transactionManager;
    }
}