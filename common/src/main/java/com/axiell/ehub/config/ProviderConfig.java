package com.axiell.ehub.config;

import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.metrics.MetricsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:provider.properties")
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

    @Bean
    public MetricsFeature metricsFeature(final MetricsProvider metricsProvider) {
        return new MetricsFeature(metricsProvider);
    }
}
