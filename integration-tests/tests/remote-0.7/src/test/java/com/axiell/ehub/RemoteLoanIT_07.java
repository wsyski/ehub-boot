package com.axiell.ehub;

import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.test.TestDataConstants;

public class RemoteLoanIT_07 extends AbstractRemoteLoanIT {
    private IEhubService ehubService;
    private PendingLoan pendingLoan;

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void givenPendingLoan() {
        pendingLoan =
                new PendingLoan(TestDataConstants.LMS_RECORD_ID, CONTENT_PROVIDER_NAME, TestDataConstants.ELIB_RECORD_0_ID, TestDataConstants.ELIB_FORMAT_0_ID);
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
