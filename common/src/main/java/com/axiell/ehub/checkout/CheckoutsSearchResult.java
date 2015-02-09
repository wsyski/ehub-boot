package com.axiell.ehub.checkout;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.search.SearchResultDTO;

import java.util.ArrayList;
import java.util.List;

public class CheckoutsSearchResult {
    private final SearchResultDTO<CheckoutMetadataDTO> searchResultDTO;

    public CheckoutsSearchResult() {
        this(new SearchResultDTO<CheckoutMetadataDTO>().items(new ArrayList<CheckoutMetadataDTO>()));
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

    SearchResultDTO<CheckoutMetadataDTO> toDTO() {
        return searchResultDTO;
    }
}
