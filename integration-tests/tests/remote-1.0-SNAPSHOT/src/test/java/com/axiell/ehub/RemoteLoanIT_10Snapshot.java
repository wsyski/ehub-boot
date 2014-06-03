package com.axiell.ehub;

import com.axiell.ehub.loan.PendingLoan;

public class RemoteLoanIT_10Snapshot extends AbstractRemoteLoanIT {
    private IEhubService ehubService;
    private PendingLoan pendingLoan;

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
        actualReadyLoan = ehubService.createLoan(authInfo, pendingLoan, LANGUAGE);
    }

    @Override
    protected void whenGetReadyLoanByLmsLoandId() throws EhubException {
        actualReadyLoan = ehubService.getReadyLoan(authInfo, lmsLoanId, LANGUAGE);
    }

    @Override
    protected void whenGetReadyLoanByReadyLoanId() throws EhubException {
        actualReadyLoan = ehubService.getReadyLoan(authInfo, readyLoanId, LANGUAGE);
    }
}
