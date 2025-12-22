package com.axiell.ehub.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurerDelegate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurers;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.sql.Driver;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
@PropertySource("classpath:persistence.properties")
public class DataSourceConfig {

    @Value("${connection.name}")
    private String name;

    @Value("${connection.username}")
    private String username;

    @Value("${connection.password}")
    private String password;

    @Value("${connection.url}")
    private String url;

    @Value("${connection.driver_class}")
    private String driverClass;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName(name)
                .setDatabaseConfigurer(EmbeddedDatabaseConfigurers.customizeConfigurer(HSQL, this::customize))
                .build();
    }

    private EmbeddedDatabaseConfigurer customize(EmbeddedDatabaseConfigurer defaultConfigurer) {
        return new EmbeddedDatabaseConfigurerDelegate(defaultConfigurer) {

            @Override
            public void configureConnectionProperties(final @NonNull ConnectionProperties connectionProperties, final @NonNull String databaseName) {
                super.configureConnectionProperties(connectionProperties, databaseName);
                connectionProperties.setUsername(username);
                connectionProperties.setPassword(password);
                connectionProperties.setUrl(url);
                try {
                    connectionProperties.setDriverClass((Class<? extends Driver>) ClassUtils.forName(driverClass, getClass().getClassLoader()));
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex.getMessage(), ex);
                }
            }
        };
    }
}
