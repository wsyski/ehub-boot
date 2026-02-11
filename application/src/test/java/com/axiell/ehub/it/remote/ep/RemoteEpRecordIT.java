package com.axiell.ehub.it.remote.ep;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.it.config.EhubServiceClientConfig;
import com.axiell.ehub.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.it.remote.RemoteRecordITFixture;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.config.TestDataRestApiServerConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest(classes = {EhubApplication.class, TestDataRestApiServerConfig.class, EhubServiceClientConfig.class, TestDataServiceClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile({"default","dev"})
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
