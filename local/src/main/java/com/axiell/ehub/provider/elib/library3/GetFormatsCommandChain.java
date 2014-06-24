package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.*;

import java.util.List;

import static com.axiell.ehub.provider.elib.library3.Product.AvailableFormat;

class GetFormatsCommandChain extends AbstractElib3CommandChain<Formats, Elib3CommandData> {
    private final GetProductCommand firstCommand;
    private final BookAvailabilityCommand bookAvailabilityCommand;
    private final LibraryProductCommand libraryProductCommand;
    private final IFormatFactory formatFactory;

    GetFormatsCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory, final IFormatFactory formatFactory) {
        super(elibFacade, exceptionFactory);
        this.formatFactory = formatFactory;
        firstCommand = new GetProductCommand(elibFacade, exceptionFactory);
        bookAvailabilityCommand = new BookAvailabilityCommand(elibFacade, exceptionFactory);
        libraryProductCommand = new LibraryProductCommand(elibFacade, exceptionFactory);

        configureFirstCommand();
        configureBookAvailabilityCommand();
        configureLibraryProductCommand();
    }

    private void configureFirstCommand() {
        firstCommand.next(bookAvailabilityCommand);
    }

    private void configureBookAvailabilityCommand() {
        bookAvailabilityCommand.next(libraryProductCommand);
    }

    private void configureLibraryProductCommand() {
        libraryProductCommand.next(END_COMMAND);
    }

    @Override
    public Formats execute(final Elib3CommandData data) {
        firstCommand.run(data);
        return makeFormats(data);
    }

    private Formats makeFormats(final Elib3CommandData data) {
        final List<AvailableFormat> availableFormats = data.getAvailableFormats();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final Formats formats = new Formats();

        for (AvailableFormat availableFormat : availableFormats) {
            final String formatId = availableFormat.getId();
            final Format format = formatFactory.create(contentProvider, formatId, language);
            formats.addFormat(format);
        }

        return formats;
    }
}
