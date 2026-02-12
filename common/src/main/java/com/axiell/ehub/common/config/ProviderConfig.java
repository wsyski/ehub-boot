package com.axiell.ehub.common.config;

import com.axiell.ehub.common.controller.provider.json.JsonProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ehub.properties")
public class ProviderConfig {
    @Value("${logging.verbose}")
    private boolean verbose;
    @Value("${logging.prettyLogging}")
    private boolean prettyLogging;

    @Bean
    public JacksonJsonProvider jacksonJsonProvider() {
        return new JsonProvider();
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(prettyLogging);
        loggingFeature.setVerbose(verbose);
        return loggingFeature;
    }
}
