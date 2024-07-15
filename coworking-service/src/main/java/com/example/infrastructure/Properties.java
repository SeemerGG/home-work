package com.example.infrastructure;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Properties {

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${liquibase.defaultSchema}")
    private String defaultSchema;

    @Value("${liquibase.liquibaseschema}")
    private String liquibaseSchema;

    @Value("${liquibase.changeLogPath}")
    private String changeLogPath;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.tokenLifetime}")
    private String tokenLifetime;

    private Map<String, String> properties;

    public Properties() {
        properties = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        Field[] fields = Properties.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                try {
                    field.setAccessible(true);
                    properties.put(field.getName(), (String) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getProperty(String key) {
        
        return properties.get(key);
    }
}
