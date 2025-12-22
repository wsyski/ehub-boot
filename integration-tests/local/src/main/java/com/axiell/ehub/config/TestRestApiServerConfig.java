package com.axiell.ehub.config;

import com.axiell.ehub.controller.internal.TestRootResource;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class TestRestApiServerConfig {
    @Autowired
    private Bus bus;

    @Bean
    public Server testRestApiServer(
            final TestRootResource testRootResource,
            final JsonProvider jsonProvider) {
        final JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/test");
        endpoint.setServiceBean(testRootResource);
        endpoint.setProvider(jsonProvider);
        endpoint.setProvider(Collections.singletonList(new LoggingFeature()));
        return endpoint.create();
    }
}
