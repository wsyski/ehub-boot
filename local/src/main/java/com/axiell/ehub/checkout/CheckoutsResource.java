package com.axiell.ehub.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

import java.util.List;

public class CheckoutsResource implements ICheckoutsResource {
    private final ILoanBusinessController loanBusinessController;

    public CheckoutsResource(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Override
    public SearchResultDTO<CheckoutMetadataDTO> search(AuthInfo authInfo, String lmsLoanId, String language) {
        CheckoutsSearchResult checkoutsSearchResult = loanBusinessController.search(authInfo, lmsLoanId, language);
        SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = new SearchResultDTO<>();
        List<CheckoutMetadataDTO> itemsDTO = checkoutsSearchResult.items();
        int size = itemsDTO.size();
        searchResultDTO.items(itemsDTO).limit(size).offset(0).totalItems(size);
        return searchResultDTO;
    }

    @Override
    public CheckoutDTO checkout(final AuthInfo authInfo, final FieldsDTO fields, final String language) {
        PendingLoan pendingLoan = new PendingLoan(fields.getFields().get("lmsRecordId"), fields.getFields().get("contentProviderName"),
                fields.getFields().get("contentProviderRecordId"), fields.getFields().get("contentProviderFormat"));
        Checkout checkout = loanBusinessController.checkout(authInfo, pendingLoan, language);
        return checkout.toDTO();
    }

    @Override
    public CheckoutDTO getCheckout(final AuthInfo authInfo, final Long ehubCheckoutId, final String language) {
        Checkout checkout = loanBusinessController.getCheckout(authInfo, ehubCheckoutId, language);
        return checkout.toDTO();
    }
}
