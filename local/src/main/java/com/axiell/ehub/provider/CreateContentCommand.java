package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import static com.axiell.ehub.provider.CreateContentCommand.Result.CONTENT_CREATED;

public class CreateContentCommand extends AbstractCommand<CommandData> {
    private final IContentFactory contentFactory;

    public CreateContentCommand(final IContentFactory contentFactory) {
        this.contentFactory = contentFactory;
    }

    @Override
    public void run(final CommandData data) {
        final String contentUrl = data.getContentUrl();
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = loanMetadata.getFormatDecoration();
        final ContentLink content = contentFactory.create(contentUrl, formatDecoration);
        data.setContent(content);
        forward(CONTENT_CREATED, data);
    }

    public static enum Result implements ICommandResult {
        CONTENT_CREATED
    }
}
