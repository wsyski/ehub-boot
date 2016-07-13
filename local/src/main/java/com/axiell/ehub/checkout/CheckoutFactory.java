package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckoutFactory implements ICheckoutFactory {

    @Autowired
    private ICheckoutMetadataFactory checkoutMetadataFactory;

    @Override
    public Checkout create(final EhubLoan ehubLoan, final FormatDecoration formatDecoration, final Content content,
                           final String language, final boolean isNewLoan) {
        CheckoutMetadata checkoutMetadata = checkoutMetadataFactory.create(ehubLoan, formatDecoration, language, isNewLoan);
        return new Checkout(checkoutMetadata, content);
    }
}
