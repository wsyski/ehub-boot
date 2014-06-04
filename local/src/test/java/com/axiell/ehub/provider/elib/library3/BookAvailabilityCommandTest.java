package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.*;
import static com.axiell.ehub.provider.ContentProviderName.ELIB3;
import static com.axiell.ehub.provider.elib.library3.BookAvailabilityCommand.Result.AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD;
import static com.axiell.ehub.provider.elib.library3.BookAvailabilityCommand.Result.PRODUCT_AVAILABLE;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class BookAvailabilityCommandTest extends AbstractElib3CommandTest {
    private BookAvailabilityCommand underTest;
    @Mock
    private BookAvailability bookAvailability;

    @Before
    public void setUp() {
        underTest = new BookAvailabilityCommand(elibFacade, exceptionFactory);
    }

    @Test
    public void noLibraryCard() {
        givenBasicCommandData();
        givenCommandOnNoCard();
        whenRun();
        thenCommandIsInvoked();
    }

    @Test
    public void availableProduct() {
        givenLibraryCard();
        givenBasicCommandData();
        givenProductIsAvailable();
        givenBookAvailability();
        givenCommandOnProductAvailable();
        whenRun();
        thenCommandIsInvoked();
    }

    @Test
    public void maxNumberOfDownloadsForProductReached() {
        givenLanguage();
        givenLibraryCard();
        givenBasicCommandData();
        givenMaxNoOfDownloadsReachedInBookAvailability();
        givenBookAvailability();
        givenMaxNoOfDownloadsReachedAsArgumentType();
        givenInternalErrorServerExceptionWithMaxNoOfDownloadsReached();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithMaxNoOfDownloadsReached();
        }
    }

    @Test
    public void borrowerLimitReached() {
        givenLanguage();
        givenLibraryCard();
        givenBasicCommandData();
        givenBorrowerLimitReachedInBookAvailability();
        givenBookAvailability();
        givenBorrowerLimitReachedAsArgumentType();
        givenInternalErrorServerExceptionWithMaxNoOfDownloadsReached();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithBorrowerLimitReached();
        }
    }

    @Test
    public void libraryLimitReached() {
        givenLanguage();
        givenLibraryCard();
        givenBasicCommandData();
        givenLibraryLimitReachedInBookAvailability();
        givenBookAvailability();
        givenLibraryLimitReachedAsArgumentType();
        givenInternalErrorServerExceptionWithMaxNoOfDownloadsReached();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithLibraryLimitReached();
        }
    }

    @Test
    public void productUnavailable() {
        givenLanguage();
        givenLibraryCard();
        givenBasicCommandData();
        givenBookAvailability();
        givenInternalErrorServerExceptionWithProductUnavailable();
        try {
            whenRun();
            thenInternalServerErrorExceptionShouldHaveBeenThrown();
        } catch (InternalServerErrorException e) {
            thenInternalErrorExceptionIsCreatedWithProductUnavailable();
        }
    }

    private void givenInternalErrorServerExceptionWithProductUnavailable() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE, language)).willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithProductUnavailable() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE, language);
    }

    private void givenLibraryLimitReachedInBookAvailability() {
        given(bookAvailability.isLibraryLimitReached()).willReturn(true);
    }

    private void givenLibraryLimitReachedAsArgumentType() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, LIBRARY_LIMIT_REACHED, language)).willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithLibraryLimitReached() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, LIBRARY_LIMIT_REACHED, language);
    }

    private void thenInternalErrorExceptionIsCreatedWithBorrowerLimitReached() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, BORROWER_LIMIT_REACHED, language);
    }

    private void givenBorrowerLimitReachedAsArgumentType() {
        argValueType = BORROWER_LIMIT_REACHED;
    }

    private void givenBorrowerLimitReachedInBookAvailability() {
        given(bookAvailability.isBorrowerLimitReached()).willReturn(true);
    }

    private void givenLanguage() {
        language = LANGUAGE;
    }

    private void givenMaxNoOfDownloadsReachedInBookAvailability() {
        given(bookAvailability.isMaxNumberOfDownloadsForProductReached()).willReturn(true);
    }

    private void givenMaxNoOfDownloadsReachedAsArgumentType() {
        argValueType = MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED;
    }

    private void givenInternalErrorServerExceptionWithMaxNoOfDownloadsReached() {
        given(exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, argValueType, language)).willReturn(internalServerErrorException);
    }

    private void thenInternalErrorExceptionIsCreatedWithMaxNoOfDownloadsReached() {
        verify(exceptionFactory).createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED, language);
    }

    private void givenLibraryCard() {
        libraryCard = "card";
    }

    private void givenProductIsAvailable() {
        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
    }

    private void givenBookAvailability() {
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(bookAvailability);
    }

    private void givenCommandOnProductAvailable() {
        underTest.on(PRODUCT_AVAILABLE, next);
    }

    private void givenCommandOnNoCard() {
        underTest.on(AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD, next);
    }

    private void whenRun() {
        underTest.run(data);
    }


}