package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckoutFactory {
    @Autowired
    private IFormatFactory formatFactory;

    public Checkout create(EhubLoan ehubLoan, String language) {
//        ehubLoan.getContentProviderLoanMetadata().getFormatDecoration().get;
//        Format

        return null;
    }
}
