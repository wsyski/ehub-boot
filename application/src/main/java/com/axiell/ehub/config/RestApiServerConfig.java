package com.axiell.ehub.config;

import com.axiell.ehub.controller.external.RootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.metrics.MetricsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RestApiServerConfig {
    @Autowired
    private Bus bus;
    @Autowired
    private MetricsProvider metricsProvider;

    @Bean
    public Server restApiServer(final AuthInfoParamConverterProvider authInfoParamConverterProvider) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(Arrays.<Object>asList(new RootResource()));
        endpoint.setAddress("/");
        endpoint.setProviders(Arrays.<Object>asList(authInfoParamConverterProvider));
        endpoint.setFeatures(Arrays.asList(openApiFeature(), metricsFeature(), loggingFeature()));
        return endpoint.create();
    }

    @Bean
    public OpenApiFeature openApiFeature() {
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setPropertiesLocation("/swagger.properties");
        SwaggerUiConfig swaggerUiConfig = new SwaggerUiConfig().url("/api/openapi.json").queryConfigEnabled(false);
        openApiFeature.setSwaggerUiConfig(swaggerUiConfig);
        return openApiFeature;
    }

    @Bean
    public MetricsFeature metricsFeature() {
        return new MetricsFeature(metricsProvider);
    }

    @Bean
    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

}
