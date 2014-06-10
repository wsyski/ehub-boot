package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;

import java.util.List;

import static com.axiell.ehub.provider.elib.library3.Product.AvailableFormat;

class GetFormatsCommandChain extends AbstractElib3CommandChain<Formats, Elib3CommandData> {
    private final GetProductCommand firstCommand;
    private final BookAvailabilityCommand bookAvailabilityCommand;
    private final LibraryProductCommand libraryProductCommand;

    GetFormatsCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
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
        return getFormats(data);
    }

    private Formats getFormats(final Elib3CommandData data) {
        final List<AvailableFormat> availableFormats = data.getAvailableFormats();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final Formats formats = new Formats();

        for (AvailableFormat availableFormat : availableFormats) {
            final Format format = makeFormat(contentProvider, language, availableFormat);
            formats.addFormat(format);
        }

        return formats;
    }

    private Format makeFormat(final ContentProvider contentProvider, final String language, final AvailableFormat availableFormat) {
        final String formatId = availableFormat.getId();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final FormatTextBundle textBundle = formatDecoration == null ? null : formatDecoration.getTextBundle(language);
        final String name = textBundle == null ? formatId : textBundle.getName();
        final String description = textBundle == null ? formatId : textBundle.getDescription();
        return new Format(formatId, name, description, null);
    }
}
