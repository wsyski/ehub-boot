package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public interface ICheckoutMetadataFactory {

    CheckoutMetadata create(EhubLoan ehubLoan, FormatDecoration formatDecoration, String language, boolean isNewLoan);
}
