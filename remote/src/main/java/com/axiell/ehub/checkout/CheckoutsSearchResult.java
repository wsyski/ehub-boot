package com.axiell.ehub.checkout;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.search.SearchResultDTO;

import java.util.List;

public class CheckoutsSearchResult {
    private final List<CheckoutMetadataDTO> items;

    public CheckoutsSearchResult(SearchResultDTO<CheckoutMetadataDTO> searchResultDTO) {
        this.items = searchResultDTO.getItems();
    }

    public CheckoutMetadata findCheckoutByLmsLoanId(final String lmsLoanId) {
        for (CheckoutMetadataDTO item : items) {
            if (lmsLoanId.equals(item.getLmsLoanId()))
                return new CheckoutMetadata(item);
        }
        final ErrorCauseArgument argument = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_LOAN_ID, lmsLoanId);
        throw new NotFoundException(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, argument);
    }
}
