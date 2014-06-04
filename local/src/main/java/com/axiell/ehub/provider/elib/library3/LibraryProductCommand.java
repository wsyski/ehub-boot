package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ICommandResult;

import static com.axiell.ehub.provider.elib.library3.LibraryProductCommand.Result.MODEL_AVAILABLE;

class LibraryProductCommand extends  AbstractElib3Command<CommandData> {

    LibraryProductCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final LibraryProduct libraryProduct = elibFacade.getLibraryProduct(contentProviderConsumer, contentProviderRecordId);

        if (libraryProduct.hasAvailableModel())
            forward(MODEL_AVAILABLE, data);
//        else // TODO:

    }

    public static enum Result implements ICommandResult {
        MODEL_AVAILABLE
    }
}
