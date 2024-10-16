package com.axiell.ehub.it.ep;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLink;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.it.RemoteCheckoutITFixture;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertThat;

public abstract class RemoteEpCheckoutITFixture extends RemoteCheckoutITFixture {

    @Test
    public final void checkoutWithContentProviderError() throws EhubException {
        givenExpectedContentProviderErrorException(ErrorCauseArgumentType.ALREADY_ON_LOAN.name());
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_ID_1);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutErrorResponse(ErrorCauseArgumentType.ALREADY_ON_LOAN);
        Checkout checkout = whenCheckout();
    }

    protected void givenContentProviderCheckoutErrorResponse(final ErrorCauseArgumentType errorCauseArgumentType) {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts")).willReturn(
                aResponse().withBodyFile(getContentProviderName() + "/errorDTO_" + errorCauseArgumentType.name() + ".json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)));
    }


    protected void givenContentProviderCheckoutResponse() {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts")).willReturn(
                aResponse().withBodyFile(getResponseFilePrefix() + "checkoutResponse_newLoan.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_CREATED)));
    }

    private String getResponseFilePrefix() {
        return getContentProviderName() + (isLoanPerProduct() ? "/lpp/" : "/lpf/");
    }

    @Override
    protected void givenContentProviderGetCheckoutResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/checkouts/" + TestDataConstants.CONTENT_PROVIDER_LOAN_ID)).willReturn(
                aResponse().withBodyFile(getResponseFilePrefix() + "checkoutResponse_activeLoan.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    @Override
    protected void thenValidContentLinks(final ContentLinks contentLinks, final String contentProviderFormatId) {
        Assert.assertNotNull(contentLinks);
        Assert.assertEquals(2, contentLinks.getContentLinks().size());
        assertThat(contentLinks.getContentLinks().get(0),
                matchesExpectedContentLink(new ContentLink("http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/content_0")));
        assertThat(contentLinks.getContentLinks().get(1),
                matchesExpectedContentLink(new ContentLink("http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/content_1")));
    }

    @Override
    protected void thenValidSupplementLinks(final SupplementLinks supplementLinks, final String contentProviderFormatId) {
        Assert.assertNotNull(supplementLinks);
        Assert.assertEquals(2, supplementLinks.getSupplementLinks().size());
        assertThat(supplementLinks.getSupplementLinks().get(0),
                matchesExpectedSupplementLink(
                        new SupplementLink("supplement_0", "http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/supplement_0")));
        assertThat(supplementLinks.getSupplementLinks().get(1),
                matchesExpectedSupplementLink(
                        new SupplementLink("supplement_1", "http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/supplement_1")));
    }


    @Override
    protected String getContentProviderFormatId() {
        return TestDataConstants.TEST_EP_FORMAT_ID_0;
    }

    @Override
    protected String getIssueId() {
        return null;
    }
}
