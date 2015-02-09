package com.axiell.ehub.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

public class CheckoutsResource implements ICheckoutsResource {
    private final ILoanBusinessController loanBusinessController;

    public CheckoutsResource(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Override
    public SearchResultDTO<CheckoutMetadataDTO> search(AuthInfo authInfo, String lmsLoanId, String language) {
        return null;
    }

    @Override
    public CheckoutDTO checkout(AuthInfo authInfo, FieldsDTO fields, String language) {
        PendingLoan pendingLoan = new PendingLoan(fields.getFields().get("lmsRecordId"), fields.getFields().get("contentProviderName"),
                fields.getFields().get("contentProviderRecordId"), fields.getFields().get("contentProviderFormat"));
        Checkout checkout=loanBusinessController.checkout(authInfo, pendingLoan, language);
        return checkout.toDTO();
    }

    @Override
    public CheckoutDTO getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        return null;
    }
}
