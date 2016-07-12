package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.IFormatFactory;

import java.util.ArrayList;
import java.util.List;

import static com.axiell.ehub.provider.elib.library3.Product.AvailableFormat;

class GetFormatsCommandChain extends AbstractElib3CommandChain<List<Format>, Elib3CommandData> {
    private final GetProductCommand firstCommand;
    private final BookAvailabilityCommand bookAvailabilityCommand;
    private final GetLoansCommand getLoansCommand;
    private final LibraryProductCommand libraryProductCommand;
    private final IFormatFactory formatFactory;

    GetFormatsCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory, final IFormatFactory formatFactory) {
        super(elibFacade, exceptionFactory);
        this.formatFactory = formatFactory;
        firstCommand = new GetProductCommand(elibFacade, exceptionFactory);
        getLoansCommand = new GetLoansCommand(elibFacade, exceptionFactory);
        bookAvailabilityCommand = new BookAvailabilityCommand(elibFacade, exceptionFactory);
        libraryProductCommand = new LibraryProductCommand(elibFacade, exceptionFactory);

        configureFirstCommand();
        configureGetLoansCommand();
        configureBookAvailabilityCommand();
        configureLibraryProductCommand();
    }

    private void configureFirstCommand() {
        firstCommand.next(getLoansCommand);
    }

    private void configureGetLoansCommand() {
        getLoansCommand.on(GetLoansCommand.Result.PATRON_HAS_LOAN_WITH_PRODUCT_ID, END_COMMAND);
        getLoansCommand.on(GetLoansCommand.Result.PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID, bookAvailabilityCommand);
    }

    private void configureBookAvailabilityCommand() {
        bookAvailabilityCommand.next(libraryProductCommand);
    }

    private void configureLibraryProductCommand() {
        libraryProductCommand.next(END_COMMAND);
    }

    @Override
    public List<Format> execute(final Elib3CommandData data) {
        firstCommand.run(data);
        return makeFormats(data);
    }

    private List<Format> makeFormats(final Elib3CommandData data) {
        final List<AvailableFormat> availableFormats = data.getAvailableFormats();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final List<Format> formats = new ArrayList<>();

        for (AvailableFormat availableFormat : availableFormats) {
            final String formatId = availableFormat.getId();
            final Format format = formatFactory.create(contentProvider, formatId, language);
            formats.add(format);
        }

        return formats;
    }
}
