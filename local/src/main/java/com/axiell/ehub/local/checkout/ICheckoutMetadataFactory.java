package com.axiell.ehub.local.checkout;

import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.loan.EhubLoan;

public interface ICheckoutMetadataFactory {

    CheckoutMetadata create(EhubLoan ehubLoan, FormatDecoration formatDecoration, String language, boolean isNewLoan);
}
