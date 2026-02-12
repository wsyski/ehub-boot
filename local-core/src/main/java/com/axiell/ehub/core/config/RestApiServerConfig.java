package com.axiell.ehub.core.config;

import com.axiell.ehub.common.controller.external.IRootResource;
import com.axiell.ehub.common.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.core.controller.external.RootResource;
import com.axiell.ehub.core.controller.provider.mapper.EhubRuntimeExceptionMapper;
import com.axiell.ehub.core.controller.provider.mapper.RuntimeExceptionMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.metrics.MetricsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@Configuration
public class RestApiServerConfig {

    @Autowired
    private MetricsProvider metricsProvider;

    @Bean
    public Server restApiServer(
            final Bus bus,
            final IRootResource rootResource,
            final @Qualifier("authInfoParamConverterProviderServer") AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final EhubRuntimeExceptionMapper ehubRuntimeExceptionMapper,
            final RuntimeExceptionMapper runtimeExceptionMapper,
            final JacksonJsonProvider jacksonJsonProvider,
            final OpenApiFeature openApiFeature,
            final MetricsFeature metricsFeature,
            final LoggingFeature loggingFeature
    ) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(Collections.singletonList(rootResource));
        endpoint.setAddress("/");
        endpoint.setProviders(Arrays.asList(ehubRuntimeExceptionMapper, runtimeExceptionMapper, authInfoParamConverterProvider, jacksonJsonProvider));
        endpoint.setFeatures(Arrays.asList(openApiFeature, loggingFeature, metricsFeature));
        return endpoint.create();
    }

    @Bean
    public RuntimeExceptionMapper runtimeExceptionMapper() {
        return new RuntimeExceptionMapper();
    }

    @Bean
    public EhubRuntimeExceptionMapper ehubRuntimeExceptionMapper() {
        return new EhubRuntimeExceptionMapper();
    }

    @Bean
    public OpenApiFeature openApiFeature() {
        OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setPropertiesLocation("/swagger.properties");
        openApiFeature.setResourcePackages(Set.of(
                "com.axiell.ehub.common.controller.external",
                "com.axiell.ehub.common.controller.provider"
        ));
        openApiFeature.setScan(true);
        SwaggerUiConfig swaggerUiConfig = new SwaggerUiConfig()
                .url("/api/openapi.json")
                .queryConfigEnabled(false);
        openApiFeature.setSwaggerUiConfig(swaggerUiConfig);
        return openApiFeature;
    }

    @Bean
    public MetricsFeature metricsFeature() {
        return new MetricsFeature(metricsProvider);
    }

    @Bean
    public IRootResource rootResource() {
        return new RootResource();
    }
}


