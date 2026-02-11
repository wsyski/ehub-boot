package com.axiell.ehub.test.config;

import com.axiell.ehub.test.controller.internal.TestDataRootResource;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.metrics.MetricsFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class TestDataRestApiServerConfig {
    @Autowired
    private Bus bus;

    @Bean
    public Server testRestApiServer(
            final TestDataRootResource testRootResource,
            final JsonProvider jsonProvider,
            final MetricsFeature metricsFeature,
            final LoggingFeature loggingFeature) {
        final JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/test");
        endpoint.setServiceBean(testRootResource);
        final List<?> providers = Collections.singletonList(jsonProvider);
        final List<Feature> features = List.of(loggingFeature, metricsFeature);
        endpoint.setProviders(providers);
        endpoint.setFeatures(features);
        return endpoint.create();
    }
}
