package com.axiell.ehub;

import com.axiell.ehub.loan.PendingLoan;
import org.junit.Before;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;

public class RemoteLoanIT_07 extends AbstractRemoteLoanIT {
    private IEhubService ehubService;
    private PendingLoan pendingLoan;

    @Before
    public void givenPatronInAuthenticatePatronResponse() {
        stubFor(get(urlEqualTo("/arena.pa.palma/v267/patron?wsdl")).willReturn(aResponse().withHeader("Content-Type", "application/xml").withBodyFile("patron.wsdl")));
        stubFor(post(urlEqualTo("/arena.pa.palma/v267/patron")).withRequestBody(containing(":authenticatePatron xmlns")).willReturn(aResponse().withBodyFile("AuthenticatePatronResponse.xml").withStatus(200)));
    }

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void givenPendingLoan() {
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, CONTENT_PROVIDER_NAME, DevelopmentData.ELIB_RECORD_0_ID, DevelopmentData.ELIB_FORMAT_0_ID);
    }

    @Override
    protected void whenCreateLoan() throws EhubException {
        actualReadyLoan = ehubService.createLoan(authInfo, pendingLoan);
    }

    @Override
    protected void whenGetReadyLoanByLmsLoandId() throws EhubException {
        actualReadyLoan = ehubService.getReadyLoan(authInfo, lmsLoanId);
    }

    @Override
    protected void whenGetReadyLoanByReadyLoanId() throws EhubException {
        actualReadyLoan = ehubService.getReadyLoan(authInfo, readyLoanId);
    }
}
