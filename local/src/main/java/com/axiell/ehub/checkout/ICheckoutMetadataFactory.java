package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;

public interface ICheckoutMetadataFactory {

    CheckoutMetadata create(EhubLoan ehubLoan, String language);
}
