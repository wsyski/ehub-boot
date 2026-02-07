package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;

import static com.axiell.ehub.provider.CreateContentCommand.Result.CONTENT_CREATED;

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
