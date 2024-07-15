package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.infrastructure.Properties;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@DependsOn("properties")
public class DataSourceConfig {
    
    private final Properties properties;

    @Autowired
    public DataSourceConfig(Properties properties) {
        this.properties = properties;
    }

    @Bean
    public DataSource dataSource() {
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getProperty("dbDriver"));
        dataSource.setUrl(properties.getProperty("dbHost"));
        dataSource.setUsername(properties.getProperty("dbUser"));
        dataSource.setPassword(properties.getProperty("dbPassword"));

        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(properties.getProperty("defaultSchema"));
        liquibase.setLiquibaseSchema(properties.getProperty("liquibaseSchema"));
        liquibase.setChangeLog(properties.getProperty("changeLogPath"));
        
        return liquibase;
    }

}
