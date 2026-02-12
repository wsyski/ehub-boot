package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.checkout.ContentLinks;

import static com.axiell.ehub.local.provider.CreateContentCommand.Result.CONTENT_CREATED;

public class CreateContentCommand extends AbstractCommand<CommandData> {

    @Override
    public void run(final CommandData data) {
        final ContentLinks contentLinks = data.getContentLinks();
        data.setContent(new Content(contentLinks).supplementLinks(data.getSupplementLinks()));
        forward(CONTENT_CREATED, data);
    }

    public static enum Result implements ICommandResult {
        CONTENT_CREATED
    }
}
