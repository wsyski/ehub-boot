package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckoutMetadataFactory implements ICheckoutMetadataFactory {
    @Autowired
    private IFormatFactory formatFactory;

    public CheckoutMetadata create(EhubLoan ehubLoan, String language) {
        Long id = ehubLoan.getId();
        ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
        String contentProviderLoanId = contentProviderLoanMetadata.getId();
        Date expirationDate = contentProviderLoanMetadata.getExpirationDate();
        String lmsLoanId = ehubLoan.getLmsLoan().getId();
        FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        Format format = formatFactory.create(formatDecoration, language);
        return new CheckoutMetadata().id(id).contentProviderLoanId(contentProviderLoanId).expirationDate(expirationDate).format(format).lmsLoanId(lmsLoanId);
    }
}
