package com.axiell.ehub;

import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.metrics.MetricsFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Component
public class EhubServiceClientFactory implements IEhubServiceClientFactory {
    @Autowired
    private AuthInfoParamConverterProvider authInfoParamConverterProvider;
    @Autowired
    private JacksonJsonProvider jacksonJsonProvider;
    @Autowired
    private MetricsFeature metricsFeature;
    @Autowired
    private LoggingFeature loggingFeature;

    @Override
    public IEhubServiceClient create(final String baseUri) {
        final List<?> providers = Arrays.asList(jacksonJsonProvider, authInfoParamConverterProvider);
        final List<Feature> features = List.of(loggingFeature, metricsFeature);
        IRootResource rootResource = JAXRSClientFactory.create(baseUri, IRootResource.class, providers, features, null);
        EhubServiceClient ehubServiceClient = new EhubServiceClient();
        ehubServiceClient.setRootResource(rootResource);
        return ehubServiceClient;
    }

}
