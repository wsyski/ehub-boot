package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.List;

import static com.axiell.ehub.provider.CreateContentCommand.Result.CONTENT_CREATED;

public class CreateContentCommand extends AbstractCommand<CommandData> {
    private final IContentLinksFactory contentFactory;

    public CreateContentCommand(final IContentLinksFactory contentFactory) {
        this.contentFactory = contentFactory;
    }

    @Override
    public void run(final CommandData data) {
        final List<String> contentUrls = data.getContentUrls();
        final FormatDecoration formatDecoration = data.getFormatDecoration();
        final ContentLinks contentLinks = contentFactory.create(contentUrls, formatDecoration);
        data.setContent(new Content(contentLinks).supplementLinks(new SupplementLinks(data.getSupplementLinks())));
        forward(CONTENT_CREATED, data);
    }

    public static enum Result implements ICommandResult {
        CONTENT_CREATED
    }
}
