package com.axiell.ehub.it.ep;

import com.axiell.ehub.it.RemoteRecordITFixture;
import com.axiell.ehub.test.TestDataConstants;

public class RemoteEpRecordIT extends RemoteRecordITFixture {


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
