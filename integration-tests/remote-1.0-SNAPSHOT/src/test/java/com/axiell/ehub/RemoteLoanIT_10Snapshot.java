package com.axiell.ehub;

import javax.annotation.Resource;

import com.axiell.ehub.loan.PendingLoan;

public class RemoteLoanIT_10Snapshot extends AbstractRemoteLoanIT {
    @Resource(name = "ehubClient")
    private IEhubService ehubService;

    private PendingLoan pendingLoan;

    protected void setUpEhubClient(org.springframework.context.ApplicationContext springContext) {
	ehubService = (IEhubService) springContext.getBean("ehubClient");
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
