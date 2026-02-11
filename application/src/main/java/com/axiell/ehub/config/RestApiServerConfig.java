package com.axiell.ehub.config;

import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.external.RootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.controller.provider.mapper.EhubRuntimeExceptionMapper;
import com.axiell.ehub.controller.provider.mapper.RuntimeExceptionMapper;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.apache.cxf.metrics.MetricsFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class RestApiServerConfig {

    @Bean
    public Server restApiServer(
            final Bus bus,
            final IRootResource rootResource,
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final EhubRuntimeExceptionMapper ehubRuntimeExceptionMapper,
            final RuntimeExceptionMapper runtimeExceptionMapper,
            final JsonProvider jsonProvider,
            final OpenApiFeature openApiFeature,
            final MetricsFeature metricsFeature,
            final LoggingFeature loggingFeature
    ) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(Collections.singletonList(rootResource));
        endpoint.setAddress("/");
        endpoint.setProviders(Arrays.asList(ehubRuntimeExceptionMapper, runtimeExceptionMapper, authInfoParamConverterProvider, jsonProvider));
        endpoint.setFeatures(Arrays.asList(openApiFeature, metricsFeature, loggingFeature));
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
        SwaggerUiConfig swaggerUiConfig = new SwaggerUiConfig()
                .url("/api/openapi.json")
                .queryConfigEnabled(false);
        openApiFeature.setSwaggerUiConfig(swaggerUiConfig);
        return openApiFeature;
    }

    @Bean
    public IRootResource rootResource() {
        return new RootResource();
    }
}


