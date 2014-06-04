package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.MISSING_CONTENT_IN_LOAN;
import static com.axiell.ehub.provider.ContentProviderName.ELIB3;
import static com.axiell.ehub.provider.elib.library3.CreateLoanCommand.Result.LOAN_CREATED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class CreateLoanCommandTest extends AbstractElib3CommandTest {
    private static final String URL = "url";
    private CreateLoanCommand underTest;
    @Mock
    private CreatedLoan createdLoan;

    @Before
    public void setUp() throws Exception {
        underTest = new CreateLoanCommand(elibFacade, exceptionFactory);
    }

    @Test
    public void noContent() {
        givenBasicCommandData();
        givenCreatedLoan();
        givenMissingContentInLoanAsArgumentType();
        givenInternalErrorServerExceptionWithMissingContentInLoan();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithMissingContentInLoan();
        }
    }

    @Test
    public void loanCreated() {
        givenBasicCommandData();
        givenCreatedLoan();
        givenContentProviderFromContentProviderConsumer();
        givenProductIdInPendingLoan();
        givenContentUrlInCreatedLoan();
        givenExpirationDateInCreatedLoan();
        givenFormatDecorationForFormatId();
        givenCommandOnLoanCreated();
        whenRun();
        thenCommandIsInvoked();
    }

    private void givenProductIdInPendingLoan() {
        elibProductId = PRODUCT_ID;
        given(pendingLoan.getContentProviderRecordId()).willReturn(elibProductId);
    }

    private void givenContentUrlInCreatedLoan() {
        given(createdLoan.getFirstContentUrl()).willReturn(URL);
    }

    private void givenExpirationDateInCreatedLoan() {
        given(createdLoan.getExpirationDate()).willReturn(new Date());
    }

    private void givenCommandOnLoanCreated() {
        underTest.on(LOAN_CREATED, next);
    }

    private void givenCreatedLoan() {
        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(createdLoan);
    }

    private void givenMissingContentInLoanAsArgumentType() {
        argValueType = MISSING_CONTENT_IN_LOAN;
    }

    private void givenInternalErrorServerExceptionWithMissingContentInLoan() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, argValueType, language)).willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithMissingContentInLoan() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MISSING_CONTENT_IN_LOAN, language);
    }

    private void whenRun() {
        underTest.run(data);
    }
}
