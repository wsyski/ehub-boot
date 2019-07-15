package com.axiell.ehub.it26.zinio;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.it26.RemoteCheckoutITFixture;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.util.EhubUrlCodec;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteZinioCheckoutIT extends RemoteCheckoutITFixture {

    @Test
    public final void checkoutWithNewContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(TestDataConstants.ZINIO_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetRecordResponse();
        givenContentProviderCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, TestDataConstants.ZINIO_FORMAT_0_ID, true);
    }

    private void givenContentProviderCheckoutResponse() {
        givenLoginResponse();
        givenPatronExists();
        givenCheckoutResponse();
    }

    @Override
    protected void givenContentProviderGetCheckoutResponse() {
        givenLoginResponse();
        givenPatronExists();
    }

    private void givenContentProviderGetRecordResponse() {
        stubFor(get(urlEqualTo(
                "/zinio/api?cmd=zinio_issues_by_magazines_and_library&lib_id=" + TestDataConstants.ZINIO_LIB_ID + "&token=" + TestDataConstants.ZINIO_TOKEN +
                        "&zinio_magazine_rbid=" + TestDataConstants.RECORD_ID_0))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/zinio_issues_by_magazines_and_library.txt")
                        .withHeader("Content-Type", "text/plain")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    private void givenPatronExists() {
        stubFor(get(urlEqualTo(
                "/zinio/api?cmd=p_exists&lib_id=" + TestDataConstants.ZINIO_LIB_ID + "&token=" + EhubUrlCodec.encode(TestDataConstants.ZINIO_TOKEN) +
                        "&email=" + EhubUrlCodec.encode(TestDataConstants.EMAIL)))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/p_exists.txt")
                        .withHeader("Content-Type", "text/plain")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    private void givenLoginResponse() {
        stubFor(post(urlEqualTo("/zinio/api")).withRequestBody(containing("cmd=p_login"))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/p_login.txt")
                        .withHeader("Content-Type", "text/plain")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    private void givenCheckoutResponse() {
        stubFor(post(urlEqualTo("/zinio/api")).withRequestBody(containing("cmd=zinio_checkout_issue"))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/zinio_checkout_issue.txt")
                        .withHeader("Content-Type", "text/plain")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    @Override
    protected String getContentProviderFormatId() {
        return TestDataConstants.ZINIO_FORMAT_0_ID;
    }

    @Override
    protected String getIssueId() {
        return TestDataConstants.ISSUE_ID_0;
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
