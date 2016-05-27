package com.axiell.ehub.loan;

import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.record.format.FormatDecoration;

@RunWith(MockitoJUnitRunner.class)
public class EhubLoanRepositoryFacadeTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private IEhubLoanRepositoryFacade underTest;

    @Mock
    private IEhubLoanRepository ehubLoanRepository;
    @Mock
    private EhubConsumer ehubConsumer;
    private Long readyLoanId = 1L;
    @Mock
    private EhubLoan expectedEhubLoan;
    private EhubLoan actualEhubLoan;
    @Mock
    private LmsLoan lmsLoan;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private FormatDecoration formatDecoration;
    private long actualCount;

    @Before
    public void setUpEhubLoanRepositoryFacade() {
        underTest = new EhubLoanRepositoryFacade();
        ReflectionTestUtils.setField(underTest, "ehubLoanRepository", ehubLoanRepository);
    }

    @Test
    public void ehubLoanNotFoundByReadyLoanId() {
        givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId();

        try {
            whenGetReadyLoanByReadyLoanId();
            Assert.fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            thenErrorCauseIsLoanByIdNotFound(e);
        }
    }

    private void givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId() {
        given(ehubLoanRepository.findOne(anyLong())).willReturn(null);
    }

    private void whenGetReadyLoanByReadyLoanId() {
        actualEhubLoan = underTest.findEhubLoan(readyLoanId);
    }

    private void thenErrorCauseIsLoanByIdNotFound(NotFoundException e) {
        ErrorCause errorCause = getErrorCause(e);
        Assert.assertEquals(ErrorCause.LOAN_BY_ID_NOT_FOUND, errorCause);
    }

    private ErrorCause getErrorCause(NotFoundException e) {
        Assert.assertNotNull(e);
        EhubError ehubError = e.getEhubError();
        return ehubError.getCause();
    }

    @Test
    public void ehubLoanNotFoundByLmsLoanId() {
        givenExpectedNotFoundException();
        givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenEhubConsumerIdAndLmsLoanId();
        whenGetReadyLoanByLmsLoanId();
        thenErrorCauseIsLoanByLmsIdNotFound();
     }

    private void givenExpectedNotFoundException() {
        exception.expect(NotFoundException.class);
    }

    private void givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenEhubConsumerIdAndLmsLoanId() {
        given(ehubLoanRepository.findLoan(any(Long.class), any(String.class))).willReturn(null);
    }

    private void whenGetReadyLoanByLmsLoanId() {
        actualEhubLoan = underTest.findEhubLoan(ehubConsumer, "lmsLoanId");
    }

    private void thenErrorCauseIsLoanByLmsIdNotFound() {
        ErrorCause errorCause = getErrorCause(NotFoundException.class.cast(exception));
        Assert.assertEquals(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, errorCause);
    }

    @Test
    public void ehubLoanByReadyLoanId() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId();
        whenGetReadyLoanByReadyLoanId();
        thenActualEhubLoanEqualsExpectedEhubLoan();
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId() {
        given(ehubLoanRepository.findOne(anyLong())).willReturn(expectedEhubLoan);
    }

    private void thenActualEhubLoanEqualsExpectedEhubLoan() {
        Assert.assertEquals(expectedEhubLoan, actualEhubLoan);
    }

    @Test
    public void ehubLoanByLmsLoanId() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenEhubConsumerIdAndLmsLoanId();
        whenGetReadyLoanByLmsLoanId();
        thenActualEhubLoanEqualsExpectedEhubLoan();
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenEhubConsumerIdAndLmsLoanId() {
        given(ehubLoanRepository.findLoan(any(Long.class), any(String.class))).willReturn(expectedEhubLoan);
    }

    @Test
    public void saveEhubLoan() {
        givenEhubLoanCanBeSavedInTheEhubDatabase();
        whenEhubLoanIsSaved();
        thenActualEhubLoanEqualsExpectedEhubLoan();
    }

    private void givenEhubLoanCanBeSavedInTheEhubDatabase() {
        given(ehubLoanRepository.save(any(EhubLoan.class))).willReturn(expectedEhubLoan);
    }

    private void whenEhubLoanIsSaved() {
        actualEhubLoan = underTest.saveEhubLoan(ehubConsumer, lmsLoan, contentProviderLoan);
    }

    @Test
    public void countLoansByFormatDecoration() {
        whenCountLoansByFormatDecoration();
        thenActualCountIsZero();
    }

    private void whenCountLoansByFormatDecoration() {
        actualCount = underTest.countLoansByFormatDecoration(formatDecoration);
    }

    private void thenActualCountIsZero() {
        Assert.assertEquals(0, actualCount);
    }

    @Test
    public void countLoansByFormatDecorationWhenFormatDecorationIsNull() {
        actualCount = underTest.countLoansByFormatDecoration(null);
        thenActualCountIsZero();
    }
}
