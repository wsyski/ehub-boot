package com.axiell.ehub.local.checkout;

import com.axiell.ehub.common.checkout.Checkout;
import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.loan.EhubLoan;
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
