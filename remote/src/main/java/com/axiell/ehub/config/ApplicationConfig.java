package com.axiell.ehub.config;

import com.axiell.ehub.EhubClient;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:ehub-client.properties")
@ComponentScan(basePackages = "com.axiell.ehub")
public class ApplicationConfig {
    @Value("${client.baseUri}")
    private String baseUri;

    @Bean(name = "ehubClient")
    public IEhubService ehubClient(final LoggingFeature loggingFeature, final JsonProvider jsonProvider, final AuthInfoParamConverterProvider authInfoParamConverterProvider) {
        final List<?> providers = Arrays.asList(jsonProvider, authInfoParamConverterProvider);
        final List<Feature> features = List.of(loggingFeature);
        IRootResource rootResource = JAXRSClientFactory.create(baseUri, IRootResource.class, providers, features, null);
        EhubClient ehubClient = new EhubClient();
        ehubClient.setRootResource(rootResource);
        return ehubClient;
    }
}
