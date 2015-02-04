package com.axiell.ehub;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class AbstractRemoteLoanIT extends AbstractRemoteIT {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected String lmsLoanId;
    protected Long readyLoanId;

    @Override
    protected void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey()).libraryCard(testData.getLibraryCard())
                .pin(testData.getPin()).build();
    }

    @Test
    public final void createLoan() throws EhubException {
        givenPalmaLoanWsdl();
        givenCheckoutTestOkResponse();
        givenGetLibraryUserOrderList();
        givenCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenGetLibraryUserOrderList();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenGetLibraryUserOrderList();
        CheckoutMetadata checkoutMetadata = whenFindCheckoutMetadataByLmsLoandId();
        thenValidCheckoutMetadata(checkoutMetadata);
    }

    @Test
    public final void ehubException() {
        givenPalmaLoanWsdl();
        givenCheckoutTestErrorResponse();
        try {
            Checkout checkout = whenCheckout();
        } catch (EhubException ex) {
            Assert.assertNotNull(ex);
            thenCustomEhubExceptionValidation(ex);
        }
    }


    private void givenPalmaLoanWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("loans.wsdl")));
    }

    private void givenCheckoutTestOkResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_ok.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    private void givenGetLibraryUserOrderList() {
        stubFor(post(urlEqualTo("/webservices/getlibraryuserorderlist.asmx/GetLibraryUserOrderList"))
                .willReturn(aResponse().withBodyFile("GetLibraryUserOrderListResponse.xml").withHeader("Content-Type", "application/xml").withStatus(200)));
    }

    private void givenCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutResponse.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
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
        Assert.assertNotNull(contentLink.getHref());
    }


    private void givenLmsLoanId() {
        lmsLoanId = TestDataConstants.LMS_LOAN_ID_1;
    }


    private void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }


    private void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml").withHeader("Content-Type", "application/xml").withStatus(200)));
    }

    protected void thenCustomEhubExceptionValidation(EhubException e) {

    }

    protected abstract CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubException;

    protected abstract Checkout whenGetCheckoutByLoanId() throws EhubException;

    protected abstract Checkout whenCheckout() throws EhubException;
}
