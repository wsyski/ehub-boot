package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.List;

import static com.axiell.ehub.provider.CreateContentCommand.Result.CONTENT_CREATED;

public class CreateContentCommand extends AbstractCommand<CommandData> {
    private final IContentFactory contentFactory;

    public CreateContentCommand(final IContentFactory contentFactory) {
        this.contentFactory = contentFactory;
    }

    @Override
    public void run(final CommandData data) {
        final List<String> contentUrls = data.getContentUrls();
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = loanMetadata.getFormatDecoration();
        final ContentLinks contentLinks = contentFactory.create(contentUrls, formatDecoration);
        data.setContent(contentLinks);
        forward(CONTENT_CREATED, data);
    }

    public static enum Result implements ICommandResult {
        CONTENT_CREATED
    }
}
