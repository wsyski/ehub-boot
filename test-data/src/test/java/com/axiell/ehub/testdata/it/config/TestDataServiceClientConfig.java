package com.axiell.ehub.testdata.it.config;

import com.axiell.ehub.testdata.ITestDataServiceClient;
import com.axiell.ehub.testdata.ITestDataServiceClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

@Lazy
@TestConfiguration
@ComponentScan(basePackages = { "com.axiell.ehub.testdata"})
public class TestDataServiceClientConfig {
    @Bean
    public ITestDataServiceClient testDataServiceClient(@Value("${local.server.port}") final int port, final ITestDataServiceClientFactory testDataServiceClientFactory) {

        return testDataServiceClientFactory.create("http://localhost:" + port + "/api/test");
    }
}
