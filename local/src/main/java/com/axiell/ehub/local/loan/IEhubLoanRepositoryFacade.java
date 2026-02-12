package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;

public interface IEhubLoanRepositoryFacade {

    EhubLoan saveEhubLoan(EhubConsumer ehubConsumer, LmsLoan lmsLoan, ContentProviderLoan contentProviderLoan);

    EhubLoan findEhubLoan(EhubConsumer ehubConsumer, String lmsLoanId);

    EhubLoan findEhubLoan(long readyLoanId);

    long countLoansByFormatDecoration(FormatDecoration formatDecoration);

    void deleteByContentProviderId(long contentProviderId);
}
