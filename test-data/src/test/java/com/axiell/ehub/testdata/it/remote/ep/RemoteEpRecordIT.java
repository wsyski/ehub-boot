package com.axiell.ehub.testdata.it.remote.ep;

import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.testdata.it.config.EhubServiceClientConfig;
import com.axiell.ehub.testdata.it.config.TestDataServiceClientConfig;import com.axiell.ehub.testdata.it.remote.RemoteRecordITFixture;
import com.axiell.ehub.testdata.ITestDataServiceClient;
import com.axiell.ehub.testdata.TestDataConstants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {EhubApplication.class, EhubServiceClientConfig.class, TestDataServiceClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class RemoteEpRecordIT extends RemoteRecordITFixture {

    @Getter
    @Autowired
    private IEhubServiceClient ehubServiceClient;
    @Getter
    @Autowired
    private ITestDataServiceClient testDataServiceClient;

    @Override
    protected String getContentProviderName() {
        return TestDataConstants.CONTENT_PROVIDER_TEST_EP;
    }

    @Override
    protected boolean isLoanPerProduct() {
        return false;
    }

    @Override
    protected int getExpectedIssueCount() {
        return 1;
    }

    @Override
    protected int getExpectedFormatCount() {
        return 3;
    }

}
