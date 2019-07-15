package com.axiell.ehub.it.ep;

import com.axiell.ehub.it.RemoteRecordITFixture;
import com.axiell.ehub.test.TestDataConstants;

import javax.servlet.http.HttpServletResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteEpRecordIT extends RemoteRecordITFixture {

    @Override
    protected void givenContentProviderGetRecordResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/records/" + TestDataConstants.RECORD_ID_0))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/getRecordResponse.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

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
