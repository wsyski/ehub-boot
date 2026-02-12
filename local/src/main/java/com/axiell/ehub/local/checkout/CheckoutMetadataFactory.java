package com.axiell.ehub.local.checkout;

import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.loan.EhubLoan;
import com.axiell.ehub.local.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckoutMetadataFactory implements ICheckoutMetadataFactory {
    @Autowired
    private IFormatFactory formatFactory;

    public CheckoutMetadata create(final EhubLoan ehubLoan, final FormatDecoration formatDecoration, final String language,
                                   final boolean isNewLoan) {
        Long id = ehubLoan.getId();
        ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
        String contentProviderLoanId = contentProviderLoanMetadata.getId();
        String issueId = contentProviderLoanMetadata.getIssueId();
        String issueTitle = contentProviderLoanMetadata.getIssueTitle();
        Date expirationDate = contentProviderLoanMetadata.getExpirationDate();
        String lmsLoanId = ehubLoan.getLmsLoan().getId();
        Format format = formatFactory.create(formatDecoration, language);
        return new CheckoutMetadata(id, lmsLoanId, expirationDate, isNewLoan, format).contentProviderLoanId(contentProviderLoanId)
                .issueId(issueId).issueTitle(issueTitle);
    }
}
