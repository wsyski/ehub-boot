package com.axiell.ehub.local.checkout;

import com.axiell.ehub.common.checkout.Checkout;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.loan.EhubLoan;

public interface ICheckoutFactory {

    Checkout create(EhubLoan ehubLoan, FormatDecoration formatDecoration, Content content, String language, boolean isNewLoan);
}
