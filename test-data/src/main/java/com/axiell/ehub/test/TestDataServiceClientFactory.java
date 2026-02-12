package com.axiell.ehub.test;

import com.axiell.ehub.test.controller.internal.ITestDataRootResource;
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
public class TestDataServiceClientFactory implements ITestDataServiceClientFactory {
    @Autowired
    private JacksonJsonProvider jacksonJsonProvider;
    @Autowired
    private MetricsFeature metricsFeature;
    @Autowired
    private LoggingFeature loggingFeature;

    @Override
    public ITestDataServiceClient create(final String baseUri) {
        final List<?> providers = Arrays.asList(jacksonJsonProvider);
        final List<Feature> features = List.of(loggingFeature, metricsFeature);
        ITestDataRootResource testDataRootResource = JAXRSClientFactory.create(baseUri, ITestDataRootResource.class, providers, features, null);
        TestDataServiceClient testDataServiceClient = new TestDataServiceClient();
        testDataServiceClient.setTestDataRootResource(testDataRootResource);
        return testDataServiceClient;
    }

}
