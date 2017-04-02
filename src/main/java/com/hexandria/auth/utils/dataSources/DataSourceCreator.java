package com.hexandria.auth.utils.dataSources;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;


/**
 * Created by root on 01.04.17.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.hexandria.auth.common.user"},
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
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
                .url(environment.getProperty("db.url") + ";INIT=RUNSCRIPT FROM '/home/frozenfoot/Backend/RK2/civilization-02-2017/src/test/resources/V1__Setup.sql'")
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(){

        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getProperty("hibernate.showsql"));
        properties.put("hibernate.hbm2dll.auto", environment.getProperty("hibernate.hbm2dll.auto"));

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
    @Primary
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userEntityManagerFactory().getObject());
        return transactionManager;
    }
}