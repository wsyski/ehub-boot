package com.axiell.ehub.it.zinio;

import com.axiell.ehub.it.RemoteRecordITFixture;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.test.TestDataConstants;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteZinioRecordIT extends RemoteRecordITFixture {

    @Override
    protected void givenContentProviderGetRecordResponse() {
        stubFor(get(urlEqualTo(
                "/zinio/api?cmd=zinio_issues_by_magazines_and_library&lib_id=" + TestDataConstants.ZINIO_LIB_ID + "&token=" + TestDataConstants.ZINIO_TOKEN +
                        "&zinio_magazine_rbid=" + TestDataConstants.RECORD_ID_0))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/zinio_issues_by_magazines_and_library.txt")
                        .withHeader("Content-Type", "text/plain")
                        .withStatus(200)));
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ZINIO;
    }

    @Override
    protected boolean isLoanPerProduct() {
        return true;
    }

    @Override
    protected int getExpectedIssueCount() {
        return 8;
    }

    @Override
    protected int getExpectedFormatCount() {
        return 1;
    }
}
