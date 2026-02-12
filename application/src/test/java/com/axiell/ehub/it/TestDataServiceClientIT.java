package com.axiell.ehub.it;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.controller.internal.dto.TestDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = {EhubApplication.class, TestDataServiceClientConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestDataServiceClientIT {

    @Autowired
    private ITestDataServiceClient testDataServiceClient;

    @Test
    public void initAndDelete() {
        TestDataDTO testDataDTO = testDataServiceClient.init(TestDataConstants.CONTENT_PROVIDER_TEST_EP, true);
        Assertions.assertEquals(TestDataConstants.NAME, testDataDTO.getName());
        testDataServiceClient.delete();
    }
}
