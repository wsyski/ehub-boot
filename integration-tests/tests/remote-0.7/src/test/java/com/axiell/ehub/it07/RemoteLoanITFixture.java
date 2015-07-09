package com.axiell.ehub.it07;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class RemoteLoanITFixture extends RemoteITFixture {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected String lmsLoanId;
    protected Long readyLoanId;
    protected ReadyLoan actualReadyLoan;

    @Override
    protected void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey()).libraryCard(testData.getLibraryCard())
                .pin(testData.getPin()).build();
    }

    @Test
    public final void createLoan() throws EhubException {
        givenPendingLoan();
        givenPalmaLoanWsdl();
        givenCheckoutTestOkResponse();
        givenContentProviderCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        givenCheckoutResponse();
        whenCreateLoan();
        thenActualReadyLoanContainsExpectedComponents();
        thenCustomReadyLoanValidation();
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenContentProviderGetCheckoutResponse();
        whenGetReadyLoanByLmsLoandId();
        thenActualReadyLoanContainsExpectedComponents();
        thenCustomReadyLoanValidation();
    }


    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        whenGetReadyLoanByReadyLoanId();
        thenActualReadyLoanContainsExpectedComponents();
        thenCustomReadyLoanValidation();
    }

    @Test
    public final void ehubException() {
        givenPendingLoan();
        givenPalmaLoanWsdl();
        givenCheckoutTestErrorResponse();
        try {
            whenCreateLoan();
        } catch (EhubException e) {
            Assert.assertNotNull(e);
            thenCustomEhubExceptionValidation(e);
        }
    }

    protected abstract void givenPendingLoan();

    private void givenPalmaLoanWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("loans.wsdl")));
    }

    private void givenCheckoutTestOkResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_ok.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    private void givenContentProviderGetCheckoutResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/checkouts/" + TestDataConstants.CONTENT_PROVIDER_LOAN_ID))
                .willReturn(aResponse().withBodyFile("checkoutResponse.json").withHeader("Content-Type", "application/json").withStatus(201)));
    }

    private void givenContentProviderCheckoutResponse() {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts"))
                .willReturn(aResponse().withBodyFile("checkoutResponse.json").withHeader("Content-Type", "application/json").withStatus(200)));
    }

    private void givenCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns")).willReturn(
                aResponse().withBodyFile("CheckOutResponse.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    protected abstract void whenCreateLoan() throws EhubException;

    private void thenActualReadyLoanContainsExpectedComponents() {
        Assert.assertNotNull(actualReadyLoan);
        thenActualReadyLoanContainsContentProviderLoan();
        thenActualReadyLoanContainsLmsLoan();
    }

    private void thenActualReadyLoanContainsContentProviderLoan() {
        ContentProviderLoan contentProviderLoan = actualReadyLoan.getContentProviderLoan();
        Assert.assertNotNull(contentProviderLoan);
        IContent content = contentProviderLoan.getContent();
        Assert.assertNotNull(content);
        Date expirationDate = contentProviderLoan.getExpirationDate();
        Assert.assertNotNull(expirationDate);
        String id = contentProviderLoan.getId();
        Assert.assertNotNull(id);
    }

    private void thenActualReadyLoanContainsLmsLoan() {
        LmsLoan lmsLoan = actualReadyLoan.getLmsLoan();
        Assert.assertNotNull(lmsLoan);
        String id = lmsLoan.getId();
        Assert.assertNotNull(id);
    }

    protected void thenCustomReadyLoanValidation() {
    }


    private void givenLmsLoanId() {
        lmsLoanId = TestDataConstants.LMS_LOAN_ID;
    }

    protected abstract void whenGetReadyLoanByLmsLoandId() throws EhubException;

    private void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }

    protected abstract void whenGetReadyLoanByReadyLoanId() throws EhubException;


    private void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    protected void thenCustomEhubExceptionValidation(EhubException e) {

    }
}
