package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
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
