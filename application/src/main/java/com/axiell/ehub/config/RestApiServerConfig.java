package com.axiell.ehub.config;

import com.axiell.ehub.controller.external.RootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.metrics.MetricsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RestApiServerConfig {

    @Bean
    public Server restApiServer(
            Bus bus,
            AuthInfoParamConverterProvider authInfoParamConverterProvider,
            JsonProvider jsonProvider,
            OpenApiFeature openApiFeature,
            MetricsFeature metricsFeature,
            LoggingFeature loggingFeature
    ) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(Arrays.<Object>asList(new RootResource()));
        endpoint.setAddress("/");
        endpoint.setProviders(Arrays.asList(authInfoParamConverterProvider, jsonProvider));
        endpoint.setFeatures(Arrays.asList(openApiFeature, metricsFeature, loggingFeature));
        return endpoint.create();
    }

    @Bean
    public OpenApiFeature openApiFeature() {
        OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setPropertiesLocation("/swagger.properties");
        SwaggerUiConfig swaggerUiConfig = new SwaggerUiConfig()
                .url("/api/openapi.json")
                .queryConfigEnabled(false);
        openApiFeature.setSwaggerUiConfig(swaggerUiConfig);
        return openApiFeature;
    }

    @Bean
    public MetricsFeature metricsFeature(MetricsProvider metricsProvider) {
        return new MetricsFeature(metricsProvider);
    }

    @Bean
    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

}


