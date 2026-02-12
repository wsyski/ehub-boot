package com.axiell.ehub.client;

import com.axiell.ehub.common.controller.external.IRootResource;
import com.axiell.ehub.common.controller.provider.converter.AuthInfoParamConverterProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Component
public class EhubServiceClientFactory implements IEhubServiceClientFactory {
    @Autowired
    @Qualifier("authInfoParamConverterProviderClient")
    private AuthInfoParamConverterProvider authInfoParamConverterProvider;
    @Autowired
    private JacksonJsonProvider jacksonJsonProvider;

    @Autowired
    private LoggingFeature loggingFeature;

    @Override
    public IEhubServiceClient create(final String baseUri) {
        final List<?> providers = Arrays.asList(jacksonJsonProvider, authInfoParamConverterProvider);
        final List<Feature> features = Collections.singletonList(loggingFeature);
        IRootResource rootResource = JAXRSClientFactory.create(baseUri, IRootResource.class, providers, features, null);
        EhubServiceClient ehubServiceClient = new EhubServiceClient();
        ehubServiceClient.setRootResource(rootResource);
        return ehubServiceClient;
    }
}
