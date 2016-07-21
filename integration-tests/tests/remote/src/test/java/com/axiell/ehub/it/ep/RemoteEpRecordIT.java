package com.axiell.ehub.it.ep;

import com.axiell.ehub.it.RemoteRecordITFixture;
import com.axiell.ehub.test.TestDataConstants;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteEpRecordIT extends RemoteRecordITFixture {

    @Override
    protected void givenContentProviderGetFormatsResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/records/" + TestDataConstants.RECORD_0_ID))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/getRecordResponse.json").withHeader("Content-Type", "application/json")
                        .withStatus(200)));
    }

    @Override
    protected String getContentProviderName() {
        return TestDataConstants.CONTENT_PROVIDER_TEST_EP;
    }

    @Override
    protected boolean isLoanPerProduct() {
        return false;
    }
}
