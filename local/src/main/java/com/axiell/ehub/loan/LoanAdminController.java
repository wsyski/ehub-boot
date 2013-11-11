package com.axiell.ehub.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.provider.record.format.FormatDecoration;

public class LoanAdminController implements ILoanAdminController {
    
    @Autowired(required = true)
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;

    @Override
    @Transactional(readOnly = true)
    public long countLoansByFormatDecoration(final FormatDecoration formatDecoration) {
	return ehubLoanRepositoryFacade.countLoansByFormatDecoration(formatDecoration);
    }
}
