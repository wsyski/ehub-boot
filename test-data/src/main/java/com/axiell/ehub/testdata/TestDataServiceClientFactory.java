package com.axiell.ehub.testdata;

import com.axiell.ehub.testdata.controller.internal.ITestDataRootResource;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Component
public class TestDataServiceClientFactory implements ITestDataServiceClientFactory {
    @Autowired
    private JacksonJsonProvider jacksonJsonProvider;
    @Autowired
    private LoggingFeature loggingFeature;

    @Override
    public ITestDataServiceClient create(final String baseUri) {
        final List<?> providers = Collections.singletonList(jacksonJsonProvider);
        final List<Feature> features = Collections.singletonList(loggingFeature);
        ITestDataRootResource testDataRootResource = JAXRSClientFactory.create(baseUri, ITestDataRootResource.class, providers, features, null);
        TestDataServiceClient testDataServiceClient = new TestDataServiceClient();
        testDataServiceClient.setTestDataRootResource(testDataRootResource);
        return testDataServiceClient;
    }

}
