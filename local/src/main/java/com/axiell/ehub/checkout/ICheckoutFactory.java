package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;

public interface ICheckoutFactory {

    Checkout create(EhubLoan ehubLoan, Content content, String language);
}
