package com.axiell.ehub.config;

import com.axiell.ehub.controller.RootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.controller.provider.mapper.EhubRuntimeExceptionMapper;
import com.axiell.ehub.controller.provider.mapper.RuntimeExceptionMapper;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Arrays;

@Configuration
public class RestApiServerConfig {
    @Bean(destroyMethod = "shutdown")
    SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    @DependsOn("cxf")
    public Server restApiServer(
            final Bus bus,
            final RootResource rootResource,
            final JsonProvider jsonProvider,
            final OpenApiFeature openApiFeature,
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final RuntimeExceptionMapper runtimeExceptionMapper,
            final EhubRuntimeExceptionMapper ehubRuntimeExceptionMapper) {
        final JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/");
        endpoint.setServiceBean(rootResource);
        endpoint.setProvider(authInfoParamConverterProvider);
        endpoint.setProvider(jsonProvider);
        endpoint.setProvider(runtimeExceptionMapper);
        endpoint.setProvider(ehubRuntimeExceptionMapper);
        endpoint.setProvider(Arrays.asList(openApiFeature, new LoggingFeature()));
        return endpoint.create();
    }

    @Bean
    public OpenApiFeature openApiFeature() {
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setTitle("Spring Boot Ehub CXF REST Application");
        openApiFeature.setContactName("Axiell");
        openApiFeature.setDescription(
                "This sample project demonstrates how to use CXF JAX-RS services"
                        + " with Spring Boot. This demo has two JAX-RS class resources being"
                        + " deployed in a single JAX-RS endpoint.");
        openApiFeature.setVersion("5.0.0");
        openApiFeature.setUseContextBasedConfig(true);
        openApiFeature.setScan(false);
        openApiFeature.setScannerClass("io.swagger.v3.jaxrs2.integration.JaxrsApplicationScanner");
        // SwaggerUiConfig swaggerUiConfig = new SwaggerUiConfig();
        // SwaggerUiConfig swaggerUiConfig = new SwaggerUiConfig().url("/openapi.json");
        // openApiFeature.setSwaggerUiConfig(swaggerUiConfig);
        return openApiFeature;
    }
}
