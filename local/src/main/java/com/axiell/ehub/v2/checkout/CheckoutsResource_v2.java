package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.Fields;
import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.auth.AuthInfo;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutsResource_v2 implements ICheckoutsResource_v2 {
    private final ILoanBusinessController loanBusinessController;

    public CheckoutsResource_v2(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Override
    public SearchResultDTO<CheckoutMetadataDTO_v2> search(AuthInfo authInfo, String lmsLoanId, String language) {
        final CheckoutsSearchResult searchResult = loanBusinessController.search(authInfo, lmsLoanId, language);
        final SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = searchResult.toDTO();
        List<CheckoutMetadataDTO_v2> items = searchResultDTO.getItems().stream().map(CheckoutMetadataDTO_v2::fromDTO).collect(Collectors.toList());
        return new SearchResultDTO<CheckoutMetadataDTO_v2>().items(items).limit(searchResultDTO.getLimit()).offset(searchResultDTO.getOffset())
                .totalItems(searchResultDTO.getTotalItems());
    }

    @Override
    public CheckoutDTO_v2 checkout(AuthInfo authInfo, FieldsDTO fieldsDTO, String language) {
        Fields fields = new Fields(fieldsDTO);
        Checkout checkout = loanBusinessController.checkout(authInfo, fields, language);
        return CheckoutDTO_v2.fromDTO(checkout.toDTO());
    }

    @Override
    public CheckoutDTO_v2 getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        Checkout checkout = loanBusinessController.getCheckout(authInfo, ehubCheckoutId, language);
        return CheckoutDTO_v2.fromDTO(checkout.toDTO());
    }
}
