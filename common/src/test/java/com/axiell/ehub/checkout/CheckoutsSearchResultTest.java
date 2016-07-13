package com.axiell.ehub.checkout;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.search.SearchResultDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
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
        assertEquals(LMS_LOAN_ID, actCheckout.getLmsLoanId());
    }

    @Test
    public void findCheckoutByLmsLoanId_notFound() {
        givenListOfCheckoutDTOs();
        givenCheckoutsInstance();
        try {
            whenFindByLmsLoanId();
            fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            EhubError ehubError = e.getEhubError();
            ErrorCause errorCause = ehubError.getCause();
            assertEquals(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, errorCause);
        }
    }
}
