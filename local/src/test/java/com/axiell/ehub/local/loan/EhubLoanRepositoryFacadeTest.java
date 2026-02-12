package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.EhubError;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.NotFoundException;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EhubLoanRepositoryFacadeTest {
    private static final Long READY_LOAN_ID = 1L;
    private static final Long EHUB_CONSUMER_ID = 1L;

    private IEhubLoanRepositoryFacade underTest;

    @Mock
    private IEhubLoanRepository ehubLoanRepository;
    @Mock
    private EhubConsumer ehubConsumer;
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

    @BeforeEach
    public void setUpEhubLoanRepositoryFacade() {
        underTest = new EhubLoanRepositoryFacade();
        ReflectionTestUtils.setField(underTest, "ehubLoanRepository", ehubLoanRepository);
    }

    @BeforeEach
    public void setUpEhubConsumer() {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
    }

    @Test
    public void ehubLoanNotFoundByReadyLoanId() {
        givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId();

        try {
            whenGetReadyLoanByReadyLoanId();
            Assertions.fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            thenErrorCauseIsLoanByIdNotFound(e);
        }
    }

    private void givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId() {
        given(ehubLoanRepository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void whenGetReadyLoanByReadyLoanId() {
        actualEhubLoan = underTest.findEhubLoan(READY_LOAN_ID);
    }

    private void thenErrorCauseIsLoanByIdNotFound(NotFoundException e) {
        ErrorCause errorCause = getErrorCause(e);
        Assertions.assertEquals(ErrorCause.LOAN_BY_ID_NOT_FOUND, errorCause);
    }

    private ErrorCause getErrorCause(NotFoundException e) {
        Assertions.assertNotNull(e);
        EhubError ehubError = e.getEhubError();
        return ehubError.getCause();
    }

    @Test
    public void ehubLoanNotFoundByLmsLoanId() {
        givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenEhubConsumerIdAndLmsLoanId();
        Exception exception = Assertions.assertThrows(NotFoundException.class, this::whenGetReadyLoanByLmsLoanId);
        thenErrorCauseIsLoanByLmsIdNotFound(exception);
    }

    private void givenNoEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenEhubConsumerIdAndLmsLoanId() {
        given(ehubLoanRepository.findLoan(any(Long.class), any(String.class))).willReturn(null);
    }

    private void whenGetReadyLoanByLmsLoanId() {
        actualEhubLoan = underTest.findEhubLoan(ehubConsumer, "lmsLoanId");
    }

    private void thenErrorCauseIsLoanByLmsIdNotFound(Exception exception) {
        ErrorCause errorCause = getErrorCause(NotFoundException.class.cast(exception));
        Assertions.assertEquals(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, errorCause);
    }

    @Test
    public void ehubLoanByReadyLoanId() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId();
        whenGetReadyLoanByReadyLoanId();
        thenActualEhubLoanEqualsExpectedEhubLoan();
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseForTheGivenReadyLoanId() {
        given(ehubLoanRepository.findById(anyLong())).willReturn(Optional.ofNullable(expectedEhubLoan));
    }

    private void thenActualEhubLoanEqualsExpectedEhubLoan() {
        Assertions.assertEquals(expectedEhubLoan, actualEhubLoan);
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
        Assertions.assertEquals(0, actualCount);
    }

    @Test
    public void countLoansByFormatDecorationWhenFormatDecorationIsNull() {
        actualCount = underTest.countLoansByFormatDecoration(null);
        thenActualCountIsZero();
    }
}
