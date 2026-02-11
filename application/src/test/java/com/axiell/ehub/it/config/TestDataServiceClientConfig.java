package com.axiell.ehub.it.config;

import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.IEhubServiceClientFactory;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.ITestDataServiceClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@Lazy
@TestConfiguration
public class TestDataServiceClientConfig {
    @Bean
    public ITestDataServiceClient testDataServiceClient(@Value("${local.server.port}") int port, ITestDataServiceClientFactory testDataServiceClientFactory) {

        return testDataServiceClientFactory.create("http://localhost:" + port + "/api/test");
    }
}
