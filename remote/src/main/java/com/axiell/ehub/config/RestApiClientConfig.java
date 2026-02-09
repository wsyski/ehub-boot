package com.axiell.ehub.config;

import com.axiell.ehub.EhubClient;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.metrics.MetricsFeature;
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
public class RestApiClientConfig {
    @Value("${client.baseUri}")
    private String baseUri;

    @Bean
    public IEhubService ehubClient(
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final JsonProvider jsonProvider,
            final MetricsFeature metricsFeature,
            final LoggingFeature loggingFeature) {
        final List<?> providers = Arrays.asList(jsonProvider, authInfoParamConverterProvider);
        final List<Feature> features = List.of(loggingFeature, metricsFeature);
        IRootResource rootResource = JAXRSClientFactory.create(baseUri, IRootResource.class, providers, features, null);
        EhubClient ehubClient = new EhubClient();
        ehubClient.setRootResource(rootResource);
        return ehubClient;
    }
}
