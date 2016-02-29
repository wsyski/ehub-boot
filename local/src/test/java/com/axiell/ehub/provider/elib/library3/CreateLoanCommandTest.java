package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.MISSING_CONTENT_IN_LOAN;
import static com.axiell.ehub.provider.elib.library3.CreateLoanCommand.Result.LOAN_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        givenProductIdInPendingLoan();
        givenBasicCommandData();
        givenCreatedLoan();
        givenContentProviderFromContentProviderConsumer();
        givenContentUrlInCreatedLoan();
        givenExpirationDateInCreatedLoan();
        givenFormatDecorationForFormatId();
        givenCommandOnLoanCreated();
        whenRun();
        thenCommandIsInvoked();
        thenCommandDataContainsContentUrl();
        thenCommandDataContainsContentProviderLoanMetadata();
    }

    private void thenCommandDataContainsContentUrl() {
        assertEquals(URL, data.getContentUrls().get(0));
    }

    private void thenCommandDataContainsContentProviderLoanMetadata() {
        assertNotNull(data.getContentProviderLoanMetadata());
    }

    private void givenProductIdInPendingLoan() {
        elibProductId = PRODUCT_ID;
        given(pendingLoan.contentProviderRecordId()).willReturn(elibProductId);
    }

    private void givenContentUrlInCreatedLoan() {
        given(createdLoan.getContentUrlsFor(any(String.class))).willReturn(Collections.singletonList(URL));
    }

    private void givenExpirationDateInCreatedLoan() {
        given(createdLoan.getExpirationDate()).willReturn(new Date());
    }

    private void givenCommandOnLoanCreated() {
        underTest.on(LOAN_CREATED, next);
    }

    private void givenCreatedLoan() {
        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(Patron.class))).willReturn(createdLoan);
    }

    private void givenMissingContentInLoanAsArgumentType() {
        argValueType = MISSING_CONTENT_IN_LOAN;
    }

    private void givenInternalErrorServerExceptionWithMissingContentInLoan() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, argValueType, language))
                .willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithMissingContentInLoan() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MISSING_CONTENT_IN_LOAN, language);
    }

    private void whenRun() {
        underTest.run(data);
    }
}
