package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.ContentLink;

import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteLoanIT extends RemoteITFixture {
    private Fields fields;
    private String lmsLoanId;
    private Long readyLoanId;

    @Before
    public void initFields() {
        fields = new Fields();
        fields.addValue("lmsRecordId", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderAlias", CONTENT_PROVIDER_ALIAS);
        fields.addValue("contentProviderRecordId", TestDataConstants.TEST_EP_RECORD_0_ID);
        fields.addValue("contentProviderFormatId", TestDataConstants.TEST_EP_FORMAT_0_ID);
    }

    @Test
    public final void checkout() throws EhubException {
        givenPalmaLoanWsdl();
        givenPalmaCheckoutTestOkResponse();
        givenContentProviderCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        givenPalmaCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenContentProviderGetCheckoutResponse();
        CheckoutMetadata checkoutMetadata = whenFindCheckoutMetadataByLmsLoandId();
        thenValidCheckoutMetadata(checkoutMetadata);
    }

    @Test
    public final void ehubException() {
        givenPalmaLoanWsdl();
        givenCheckoutTestErrorResponse();
        try {
            whenCheckout();
        } catch (EhubException ex) {
            Assert.assertNotNull(ex);
        }
    }


    private void givenPalmaLoanWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("loans.wsdl")));
    }

    private void givenPalmaCheckoutTestOkResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_ok.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    private void givenPalmaCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutResponse.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    private void givenContentProviderGetCheckoutResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/checkouts/" + TestDataConstants.CONTENT_PROVIDER_LOAN_ID))
                .willReturn(aResponse().withBodyFile("checkoutResponse.json").withHeader("Content-Type", "application/json").withStatus(201)));
    }

    private void givenContentProviderCheckoutResponse() {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts"))
                .willReturn(aResponse().withBodyFile("checkoutResponse.json").withHeader("Content-Type", "application/json").withStatus(200)));
    }

    private void thenValidCheckout(final Checkout checkout) {
        Assert.assertNotNull(checkout);
        thenValidCheckoutMetadata(checkout.metadata());
        thenValidContentLink(checkout.contentLink());
    }

    private void thenValidCheckoutMetadata(final CheckoutMetadata checkoutMetadata) {
        Assert.assertNotNull(checkoutMetadata);

        Date expirationDate = checkoutMetadata.expirationDate();
        Assert.assertNotNull(expirationDate);
        String lmsLoanId = checkoutMetadata.lmsLoanId();
        Assert.assertNotNull(lmsLoanId);
        Long id = checkoutMetadata.id();
        Assert.assertNotNull(id);
    }

    private void thenValidContentLink(final ContentLink contentLink) {
        Assert.assertNotNull(contentLink);
        Assert.assertNotNull(contentLink.href());
    }


    private void givenLmsLoanId() {
        lmsLoanId = TestDataConstants.LMS_LOAN_ID;
    }


    private void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }


    private void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml").withHeader("Content-Type", "application/xml").withStatus(200)));
    }

    private Checkout whenCheckout() throws EhubException {
        return underTest.checkout(authInfo, fields, LANGUAGE);
    }

    private CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubException {
        return underTest.findCheckoutByLmsLoanId(authInfo, lmsLoanId, LANGUAGE);
    }

    private Checkout whenGetCheckoutByLoanId() throws EhubException {
        return underTest.getCheckout(authInfo, readyLoanId, LANGUAGE);
    }
}
