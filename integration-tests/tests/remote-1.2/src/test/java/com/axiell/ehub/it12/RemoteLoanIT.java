package com.axiell.ehub.it12;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.test.TestDataConstants;

public class RemoteLoanIT extends RemoteLoanITFixture {
    private IEhubService ehubService;
    private PendingLoan pendingLoan;

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void givenPendingLoan() {
        pendingLoan = new PendingLoan( TestDataConstants.LMS_RECORD_ID, CONTENT_PROVIDER_ALIAS,  TestDataConstants.TEST_EP_RECORD_0_ID,  TestDataConstants.TEST_EP_FORMAT_0_ID);
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
