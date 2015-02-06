package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.stereotype.Component;

@Component
public class ContentFactory implements IContentFactory {

    @Override
    public ContentLink create(final String contentUrl, final FormatDecoration formatDecoration) {
        return new ContentLink(contentUrl);
    }
}

