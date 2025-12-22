package com.axiell.ehub.checkout;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.SearchResultDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CheckoutsSearchResultTest {
    private static final String LMS_LOAN_ID = "lmsLoanId";
    private CheckoutsSearchResult underTest;
    @Mock
    private SearchResultDTO<CheckoutMetadataDTO> searchResultDTO;
    private List<CheckoutMetadataDTO> expCheckoutsDTO;
    @Mock
    private CheckoutMetadataDTO expCheckoutMetadataDTO;
    private CheckoutMetadata actCheckout;

    @Test
    public void findCheckoutByLmsLoanId_found() {
        givenListOfCheckoutDTOs();
        givenCheckoutDTOWithExpectedLmsLoanId();
        givenCheckoutsInstance();
        whenFindByLmsLoanId();
        thenActualLmsLoanIdEqualsExpectedLmsLoanId();
    }

    protected void givenListOfCheckoutDTOs() {
        expCheckoutsDTO = new ArrayList<>();
        given(searchResultDTO.getItems()).willReturn(expCheckoutsDTO);
    }

    private void givenCheckoutDTOWithExpectedLmsLoanId() {
        given(expCheckoutMetadataDTO.getLmsLoanId()).willReturn(LMS_LOAN_ID);
        expCheckoutsDTO.add(expCheckoutMetadataDTO);
    }

    private void givenCheckoutsInstance() {
        underTest = new CheckoutsSearchResult(searchResultDTO);
    }

    private void whenFindByLmsLoanId() {
        actCheckout = underTest.findCheckoutByLmsLoanId(LMS_LOAN_ID);
    }

    private void thenActualLmsLoanIdEqualsExpectedLmsLoanId() {
        Assertions.assertEquals(LMS_LOAN_ID, actCheckout.getLmsLoanId());
    }

    @Test
    public void findCheckoutByLmsLoanId_notFound() {
        givenListOfCheckoutDTOs();
        givenCheckoutsInstance();
        try {
            whenFindByLmsLoanId();
            Assertions.fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            EhubError ehubError = e.getEhubError();
            ErrorCause errorCause = ehubError.getCause();
            Assertions.assertEquals(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, errorCause);
        }
    }
}
