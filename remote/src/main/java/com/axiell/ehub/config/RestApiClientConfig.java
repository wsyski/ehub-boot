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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.axiell.ehub")
public class RestApiClientConfig {

    @Bean
    public IEhubService ehubClient(
            @Qualifier("cxfJaxrsClientAddress")final String cxfJaxrsClientAddress,
            final AuthInfoParamConverterProvider authInfoParamConverterProvider,
            final JsonProvider jsonProvider,
            final MetricsFeature metricsFeature,
            final LoggingFeature loggingFeature) {
        final List<?> providers = Arrays.asList(jsonProvider, authInfoParamConverterProvider);
        final List<Feature> features = List.of(loggingFeature, metricsFeature);
        IRootResource rootResource = JAXRSClientFactory.create(cxfJaxrsClientAddress, IRootResource.class, providers, features, null);
        EhubClient ehubClient = new EhubClient();
        ehubClient.setRootResource(rootResource);
        return ehubClient;
    }
}
