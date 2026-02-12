package com.axiell.ehub.testdata.it;

import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.testdata.ITestDataServiceClient;
import com.axiell.ehub.testdata.TestDataConstants;
import com.axiell.ehub.testdata.config.TestDataRestApiServerConfig;
import com.axiell.ehub.testdata.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.testdata.controller.internal.dto.TestDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, TestDataServiceClientConfig.class, TestDataRestApiServerConfig.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
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
