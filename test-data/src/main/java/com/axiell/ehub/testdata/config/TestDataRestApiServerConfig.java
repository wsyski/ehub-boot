package com.axiell.ehub.testdata.config;

import com.axiell.ehub.testdata.controller.internal.ITestDataRootResource;
import com.axiell.ehub.testdata.controller.internal.TestDataRootResource;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.metrics.MetricsFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.axiell.ehub.common", "com.axiell.ehub.testdata"})
public class TestDataRestApiServerConfig {

    @Bean
    public Server testDataRestApiServer(
            final Bus bus,
            final ITestDataRootResource testDataRootResource,
            final JacksonJsonProvider jacksonJsonProvider,
            final LoggingFeature loggingFeature
    ) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(List.of(testDataRootResource));
        endpoint.setAddress("/test");
        endpoint.setProviders(Collections.singletonList(jacksonJsonProvider));
        endpoint.setFeatures(Collections.singletonList(loggingFeature));
        return endpoint.create();
    }


    @Bean
    public ITestDataRootResource testDataRootResource() {
        return new TestDataRootResource();
    }


}


