package com.axiell.ehub.it;

import com.axiell.ehub.*;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThat;

public abstract class RemoteLoanITFixture extends RemoteITFixture {
    protected Fields fields;
    protected String lmsLoanId;
    protected Long readyLoanId;

    @Before
    public void initFields() {
        fields = new Fields();
        fields.addValue("lmsRecordId", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderAlias", CONTENT_PROVIDER_ALIAS);
        fields.addValue("contentProviderRecordId", TestDataConstants.TEST_EP_RECORD_0_ID);
    }

    @Test
    public final void checkoutWithContentProviderError() throws EhubException {
        givenExpectedEhubException(ErrorCause.CONTENT_PROVIDER_ERROR.toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, TestDataConstants.CONTENT_PROVIDER_TEST_EP),
                new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS, ErrorCauseArgumentType.ALREADY_ON_LOAN)));
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_0_ID);
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestNewLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderCheckoutErrorResponse(ErrorCauseArgumentType.ALREADY_ON_LOAN);
        Checkout checkout = whenCheckout();
    }

    @Test
    public final void checkoutWithLmsError() throws EhubException {
        givenExpectedEhubException(ErrorCause.LMS_ERROR.toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, "blockedBorrCard"),
                new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, testData.getEhubConsumerId().toString())));
        givenContentProviderFormatId(TestDataConstants.TEST_EP_FORMAT_1_ID);
        givenPalmaLoansWsdl();
        givenCheckoutTestErrorResponse();
        Checkout checkout = whenCheckout();
    }

    protected void givenContentProviderGetCheckoutResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/checkouts/" + TestDataConstants.CONTENT_PROVIDER_LOAN_ID)).willReturn(
                aResponse().withBodyFile(getResponseFilePrefix() + "CheckoutResponse_activeLoan.json").withHeader("Content-Type", "application/json")
                        .withStatus(200)));
    }

    protected void givenContentProviderCheckoutResponse() {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts")).willReturn(
                aResponse().withBodyFile(getResponseFilePrefix() + "CheckoutResponse_newLoan.json").withHeader("Content-Type", "application/json")
                        .withStatus(201)));
    }

    protected void givenContentProviderCheckoutErrorResponse(final ErrorCauseArgumentType errorCauseArgumentType) {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts")).willReturn(
                aResponse().withBodyFile("errorDTO_" + errorCauseArgumentType.name() + ".json").withHeader("Content-Type", "application/json").withStatus(500)));
    }

    private String getResponseFilePrefix() {
        return isLoanPerProduct() ? "lpp" : "lpf";
    }

    protected void thenValidCheckout(final Checkout checkout, final String contentProviderFormatId, final boolean isNewLoan) {
        Assert.assertNotNull(checkout);
        thenValidCheckoutMetadata(checkout.metadata(), isNewLoan);
        thenValidSupplementLinks(checkout.supplementLinks(), contentProviderFormatId);
        thenValidContentLinks(checkout.contentLinks(), contentProviderFormatId);
    }

    protected void thenValidCheckoutMetadata(final CheckoutMetadata checkoutMetadata, final boolean isNewLoan) {
        Assert.assertNotNull(checkoutMetadata);
        Date expirationDate = checkoutMetadata.expirationDate();
        Assert.assertNotNull(expirationDate);
        String lmsLoanId = checkoutMetadata.lmsLoanId();
        Assert.assertNotNull(lmsLoanId);
        Long id = checkoutMetadata.id();
        Assert.assertNotNull(id);
        Assert.assertEquals(isNewLoan, checkoutMetadata.isNewLoan());
    }

    private void thenValidContentLinks(final ContentLinks contentLinks, final String contentProviderFormatId) {
        Assert.assertNotNull(contentLinks);
        Assert.assertEquals(2, contentLinks.getContentLinks().size());
        assertThat(contentLinks.getContentLinks().get(0),
                matchesExpectedContentLink(new ContentLink("http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/content_0")));
        assertThat(contentLinks.getContentLinks().get(1),
                matchesExpectedContentLink(new ContentLink("http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/content_1")));
    }

    private void thenValidSupplementLinks(final SupplementLinks supplementLinks, final String contentProviderFormatId) {
        Assert.assertNotNull(supplementLinks);
        Assert.assertEquals(2, supplementLinks.getSupplementLinks().size());
        assertThat(supplementLinks.getSupplementLinks().get(0),
                matchesExpectedSupplementLink(
                        new SupplementLink("supplement_0", "http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/supplement_0")));
        assertThat(supplementLinks.getSupplementLinks().get(1),
                matchesExpectedSupplementLink(
                        new SupplementLink("supplement_1", "http:/localhost:16521/ep/api/v1/records/recordId_0/" + contentProviderFormatId + "/supplement_1")));
    }

    protected void givenLmsLoanId() {
        lmsLoanId = TestDataConstants.LMS_LOAN_ID;
    }

    protected void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }

    protected void givenContentProviderFormatId(final String contentProviderFormatId) {
        fields.addValue("contentProviderFormatId", contentProviderFormatId);
    }

    protected Checkout whenCheckout() throws EhubException {
        Assert.assertNotNull(fields.getRequiredValue("contentProviderFormatId"));
        return underTest.checkout(authInfo, fields, LANGUAGE);
    }

    protected CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubException {
        return underTest.findCheckoutByLmsLoanId(authInfo, lmsLoanId, LANGUAGE);
    }

    protected Checkout whenGetCheckoutByLoanId() throws EhubException {
        return underTest.getCheckout(authInfo, readyLoanId, LANGUAGE);
    }

}
