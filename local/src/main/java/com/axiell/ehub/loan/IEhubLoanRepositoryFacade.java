package com.axiell.ehub.loan;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public interface IEhubLoanRepositoryFacade {

    EhubLoan saveEhubLoan(EhubConsumer ehubConsumer, LmsLoan lmsLoan, ContentProviderLoan contentProviderLoan);

    EhubLoan findEhubLoan(EhubConsumer ehubConsumer, String lmsLoanId);

    EhubLoan findEhubLoan(long readyLoanId);
    
    long countLoansByFormatDecoration(FormatDecoration formatDecoration);

    void deleteByContentProviderId(long contentProviderId);
}