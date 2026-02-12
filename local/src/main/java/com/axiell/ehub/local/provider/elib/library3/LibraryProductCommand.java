package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.ICommandResult;

import static com.axiell.ehub.common.ErrorCauseArgumentType.PRODUCT_UNAVAILABLE;
import static com.axiell.ehub.local.provider.elib.library3.LibraryProductCommand.Result.MODEL_AVAILABLE;

class LibraryProductCommand extends AbstractElib3Command<CommandData> {

    LibraryProductCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final LibraryProduct libraryProduct = elibFacade.getLibraryProduct(contentProviderConsumer, contentProviderRecordId);

        if (libraryProduct.hasAvailableModel())
            forward(MODEL_AVAILABLE, data);
        else
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_UNAVAILABLE, language);
    }

    public static enum Result implements ICommandResult {
        MODEL_AVAILABLE
    }
}
