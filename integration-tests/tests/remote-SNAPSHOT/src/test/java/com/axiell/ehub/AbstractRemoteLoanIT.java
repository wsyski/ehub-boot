package com.axiell.ehub;

import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.security.AuthInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class AbstractRemoteLoanIT extends AbstractRemoteIT {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected String lmsLoanId;
    protected Long readyLoanId;
    protected ReadyLoan actualReadyLoan;

    @Override
    protected void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey()).libraryCard(testData.getLibraryCard()).pin(testData.getPin()).build();
    }

    @Test
    public final void createLoan() throws EhubException {
        givenPendingLoan();
        givenPalmaLoanWsdl();
        givenCheckoutTestOkResponse();
        givenGetLibraryUserOrderList();
        givenCheckoutResponse();
        whenCreateLoan();
        thenActualReadyLoanContainsExpectedComponents();
        thenCustomReadyLoanValidation();
    }

    protected abstract void givenPendingLoan();

    private void givenPalmaLoanWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("loans.wsdl")));
    }

    private void givenCheckoutTestOkResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile("CheckOutTestResponse_ok.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    private void givenGetLibraryUserOrderList() {
        stubFor(post(urlEqualTo("/webservices/getlibraryuserorderlist.asmx/GetLibraryUserOrderList")).willReturn(aResponse().withBodyFile("GetLibraryUserOrderListResponse.xml").withHeader("Content-Type", "application/xml").withStatus(200)));
    }

    private void givenCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns")).willReturn(aResponse().withBodyFile("CheckOutResponse.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
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

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenGetLibraryUserOrderList();
        whenGetReadyLoanByLmsLoandId();
        thenActualReadyLoanContainsExpectedComponents();
        thenCustomReadyLoanValidation();
    }

    private void givenLmsLoanId() {
        lmsLoanId = DevelopmentData.LMS_LOAN_ID_1;
    }

    protected abstract void whenGetReadyLoanByLmsLoandId() throws EhubException;

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenGetLibraryUserOrderList();
        whenGetReadyLoanByReadyLoanId();
        thenActualReadyLoanContainsExpectedComponents();
        thenCustomReadyLoanValidation();
    }

    private void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }

    protected abstract void whenGetReadyLoanByReadyLoanId() throws EhubException;

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

    private void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml").withHeader("Content-Type", "application/xml").withStatus(200)));
    }

    protected void thenCustomEhubExceptionValidation(EhubException e) {

    }
}
