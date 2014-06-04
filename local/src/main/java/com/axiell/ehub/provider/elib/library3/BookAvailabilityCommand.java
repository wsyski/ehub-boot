package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ICommandResult;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.*;
import static com.axiell.ehub.provider.ContentProviderName.ELIB3;
import static com.axiell.ehub.provider.elib.library3.BookAvailabilityCommand.Result.AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD;
import static com.axiell.ehub.provider.elib.library3.BookAvailabilityCommand.Result.PRODUCT_AVAILABLE;

class BookAvailabilityCommand extends AbstractElib3Command<CommandData> {

    BookAvailabilityCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final String libraryCard = data.getLibraryCard();
        if (libraryCard == null) {
            forward(AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD, data);
            return;
        }

        retriveBookAvailability(data);
    }

    private void retriveBookAvailability(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final PendingLoan pendingLoan = data.getPendingLoan();
        final String contentProviderRecordId = pendingLoan.getContentProviderRecordId();
        final String libraryCard = data.getLibraryCard();
        final String language = data.getLanguage();
        final BookAvailability bookAvailability = elibFacade.getBookAvailability(contentProviderConsumer, contentProviderRecordId, libraryCard);

        if (bookAvailability.isProductAvailable(contentProviderRecordId))
            forward(PRODUCT_AVAILABLE, data);
        else if (bookAvailability.isMaxNumberOfDownloadsForProductReached())
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(ELIB3, MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED, language);
        else if (bookAvailability.isBorrowerLimitReached())
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(ELIB3, BORROWER_LIMIT_REACHED, language);
        else if (bookAvailability.isLibraryLimitReached())
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(ELIB3, LIBRARY_LIMIT_REACHED, language);
        else
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(ELIB3, PRODUCT_UNAVAILABLE, language);
    }

    public static enum Result implements ICommandResult {
        AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD, PRODUCT_AVAILABLE;
    }
}
