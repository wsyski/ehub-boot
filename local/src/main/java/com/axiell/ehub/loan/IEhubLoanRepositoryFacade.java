package com.axiell.ehub.loan;

import com.axiell.ehub.consumer.EhubConsumer;

public interface IEhubLoanRepositoryFacade {

    EhubLoan saveEhubLoan(EhubConsumer ehubConsumer, LmsLoan lmsLoan, ContentProviderLoan contentProviderLoan);

    EhubLoan findEhubLoan(EhubConsumer ehubConsumer, String lmsLoanId);

    EhubLoan findEhubLoan(EhubConsumer ehubConsumer, Long readyLoanId);
}