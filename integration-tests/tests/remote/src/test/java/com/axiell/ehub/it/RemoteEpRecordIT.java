package com.axiell.ehub.it;

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
}
