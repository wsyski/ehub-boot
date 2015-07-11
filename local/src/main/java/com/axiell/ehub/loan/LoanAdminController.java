package com.axiell.ehub.loan;

import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class LoanAdminController implements ILoanAdminController {

    @Autowired
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;

    @Override
    @Transactional(readOnly = true)
    public long countLoansByFormatDecoration(final FormatDecoration formatDecoration) {
        return ehubLoanRepositoryFacade.countLoansByFormatDecoration(formatDecoration);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByContentProviderId(final long contentProviderId) {
        ehubLoanRepositoryFacade.deleteByContentProviderId(contentProviderId);
    }
}
