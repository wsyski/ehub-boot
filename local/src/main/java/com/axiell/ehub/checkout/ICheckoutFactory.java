package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public interface ICheckoutFactory {

    Checkout create(EhubLoan ehubLoan, FormatDecoration formatDecoration, Content content, String language);
}
