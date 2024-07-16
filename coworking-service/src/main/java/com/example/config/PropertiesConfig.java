package com.example.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;



/**
 * Класс хранящий объект Properties для удобного обращения к свойствам.
 */
@Configuration
public class PropertiesConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer ymlProperties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean properties = new YamlPropertiesFactoryBean();
        properties.setResources(new ClassPathResource("application.yml"));
        configurer.setProperties(properties.getObject());
        return configurer;
    }
}
