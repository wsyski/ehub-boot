package com.axiell.ehub.loan;

import org.springframework.stereotype.Component;

@Component
public class ReadyLoanFactory implements IReadyLoanFactory {

    @Override
    public ReadyLoan createReadyLoan(EhubLoan ehubLoan, ContentProviderLoan contentProviderLoan) {
	final Long readyLoanId = ehubLoan.getId();
	final LmsLoan lmsLoan = ehubLoan.getLmsLoan();
	return new ReadyLoan(readyLoanId, lmsLoan, contentProviderLoan);
    }

    @Override
    public ReadyLoan createReadyLoan(EhubLoan ehubLoan, IContent content) {
	final ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
	final ContentProviderLoan contentProviderLoan = new ContentProviderLoan(contentProviderLoanMetadata, content);
	return createReadyLoan(ehubLoan, contentProviderLoan);
    }
}
