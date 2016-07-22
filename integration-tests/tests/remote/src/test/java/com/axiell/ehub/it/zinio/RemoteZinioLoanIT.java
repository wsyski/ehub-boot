package com.axiell.ehub.it.zinio;

import com.axiell.ehub.it.RemoteLoanITFixture;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.util.EhubUrlCodec;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteZinioLoanIT extends RemoteLoanITFixture {

    @Override
    protected void givenContentProviderGetCheckoutResponse() {
        stubFor(post(urlEqualTo("/zinio/api")).withRequestBody(containing("cmd=p_login"))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/p_login.txt")
                        .withHeader("Content-Type", "text/plain")
                        .withStatus(200)));
        stubFor(get(urlEqualTo(
                "/zinio/api?cmd=p_exists&lib_id=" + TestDataConstants.ZINIO_LIB_ID + "&token=" + EhubUrlCodec.encode(TestDataConstants.ZINIO_TOKEN) +
                        "&email=" + EhubUrlCodec.encode(TestDataConstants.EMAIL)))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/p_exists.txt")
                        .withHeader("Content-Type", "text/plain").withStatus(200)));
    }

    @Override
    protected String getContentProviderFormatId() {
        return TestDataConstants.ZINIO_FORMAT_0_ID;
    }

    @Override
    protected String getContentProviderIssueId() {
        return TestDataConstants.ISSUE_0_ID;
    }

    @Override
    protected boolean isLoanPerProduct() {
        return true;
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ZINIO;
    }
}
