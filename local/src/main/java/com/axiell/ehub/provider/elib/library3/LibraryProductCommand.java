package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ICommandResult;

import static com.axiell.ehub.provider.elib.library3.LibraryProductCommand.Result.PRODUCT_AVAILABLE;

class LibraryProductCommand extends  AbstractElib3Command<CommandData> {

    LibraryProductCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        // TODO: call
        LibraryProduct libraryProduct = null;

        if (libraryProduct.isAvailable())
            forward(PRODUCT_AVAILABLE, data);
//        else // TODO:

    }

    public static enum Result implements ICommandResult {
        PRODUCT_AVAILABLE
    }
}
