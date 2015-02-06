package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckoutFactory implements ICheckoutFactory {

    @Autowired
    private ICheckoutMetadataFactory checkoutMetadataFactory;

    public Checkout create(EhubLoan ehubLoan, ContentLink contentLink, String language) {
        CheckoutMetadata checkoutMetadata = checkoutMetadataFactory.create(ehubLoan, language);
        return new Checkout(checkoutMetadata, contentLink);
    }
}
