package com.axiell.ehub.controller;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.ITestDataServiceClientFactory;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.config.TestDataRestApiServerConfig;
import com.axiell.ehub.test.controller.internal.dto.TestDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, TestDataRestApiServerConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class EhubApplication_TestDataServiceClientIT {

    @Autowired
    private ITestDataServiceClient testDataServiceClient;

    @Test
    public void initDelete() {
        TestDataDTO testDataDTO= testDataServiceClient.init(TestDataConstants.CONTENT_PROVIDER_TEST_EP,true);
        Assertions.assertEquals(TestDataConstants.NAME, testDataDTO.getName());
        testDataServiceClient.delete();
    }

    @Lazy
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public ITestDataServiceClient testDataServiceClient(@Value("${local.server.port}") int port, ITestDataServiceClientFactory testDataServiceClientFactory) {

            return testDataServiceClientFactory.create("http://localhost:" + port + "/api/test");
        }
    }
}
