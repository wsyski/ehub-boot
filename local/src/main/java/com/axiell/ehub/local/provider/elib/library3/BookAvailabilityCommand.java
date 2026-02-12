package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.ICommandResult;

import static com.axiell.ehub.common.ErrorCauseArgumentType.BORROWER_LIMIT_REACHED;
import static com.axiell.ehub.common.ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED;
import static com.axiell.ehub.common.ErrorCauseArgumentType.MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED;
import static com.axiell.ehub.common.ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
import static com.axiell.ehub.local.provider.elib.library3.BookAvailabilityCommand.Result.AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD;
import static com.axiell.ehub.local.provider.elib.library3.BookAvailabilityCommand.Result.PRODUCT_AVAILABLE;

class BookAvailabilityCommand extends AbstractElib3Command<CommandData> {

    BookAvailabilityCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final Patron patron = data.getPatron();
        if (patron.hasId())
            retriveBookAvailability(data);
        else
            forward(AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD, data);
    }

    private void retriveBookAvailability(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Patron patron = data.getPatron();
        final String language = data.getLanguage();
        final BookAvailability bookAvailability = elibFacade.getBookAvailability(contentProviderConsumer, contentProviderRecordId, patron);

        if (bookAvailability.isProductAvailable(contentProviderRecordId))
            forward(PRODUCT_AVAILABLE, data);
        else if (bookAvailability.isMaxNumberOfDownloadsForProductReached(contentProviderRecordId))
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED, language);
        else if (bookAvailability.isBorrowerLimitReached(contentProviderRecordId))
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, BORROWER_LIMIT_REACHED, language);
        else if (bookAvailability.isLibraryLimitReached(contentProviderRecordId))
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, LIBRARY_LIMIT_REACHED, language);
        else
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE, language);
    }

    public static enum Result implements ICommandResult {
        AVAILABILITY_NOT_RETRIEVED_WHEN_NO_CARD, PRODUCT_AVAILABLE
    }
}
