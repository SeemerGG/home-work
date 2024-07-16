package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@DependsOn("propertiesConfig")
public class DataSourceConfig {

    
    @Value("${db.driver}")
    private String driver;

    @Value("${db.host}")
    private String host;

    @Value("${db.password}")
    private String password;

    @Value("${db.user}")
    private String user;

    @Value("${liquibase.changeLogPath}")
    private String changeLogPath;

    @Value("${liquibase.liquibaseSchema}")
    private String liquibaseSchema;

    @Value("${liquibase.defaultSchema}")
    private String defaultSchema;

    @Bean
    public DataSource dataSource() {
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(host);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setLiquibaseSchema(liquibaseSchema);
        liquibase.setChangeLog(changeLogPath);
        return liquibase;
    }

}
