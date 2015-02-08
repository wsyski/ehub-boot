package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckoutFactory implements ICheckoutFactory {

    @Autowired
    private ICheckoutMetadataFactory checkoutMetadataFactory;

    @Override
    public Checkout create(EhubLoan ehubLoan, ContentLink contentLink, String language) {
        CheckoutMetadata checkoutMetadata = checkoutMetadataFactory.create(ehubLoan, language);
        return new Checkout(checkoutMetadata, contentLink);
    }
}
