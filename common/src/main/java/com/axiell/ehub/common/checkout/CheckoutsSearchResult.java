package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.NotFoundException;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.SearchResultDTO;

import java.util.ArrayList;
import java.util.List;

public class CheckoutsSearchResult {
    private final SearchResultDTO<CheckoutMetadataDTO> searchResultDTO;

    public CheckoutsSearchResult() {
        this(new SearchResultDTO<CheckoutMetadataDTO>().items(new ArrayList<>()));
    }

    public CheckoutsSearchResult(SearchResultDTO<CheckoutMetadataDTO> searchResultDTO) {
        this.searchResultDTO = searchResultDTO;
    }

    public CheckoutsSearchResult addItem(CheckoutMetadata item) {
        searchResultDTO.getItems().add(item.toDTO());
        return this;
    }

    public CheckoutMetadata findCheckoutByLmsLoanId(final String lmsLoanId) {
        List<CheckoutMetadataDTO> items = searchResultDTO.getItems();
        for (CheckoutMetadataDTO item : items) {
            if (lmsLoanId.equals(item.getLmsLoanId()))
                return new CheckoutMetadata(item);
        }
        final ErrorCauseArgument argument = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_LOAN_ID, lmsLoanId);
        throw new NotFoundException(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, argument);
    }

    public SearchResultDTO<CheckoutMetadataDTO> toDTO() {
        return searchResultDTO;
    }
}
