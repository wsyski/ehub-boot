package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContentFactory implements IContentFactory {

    @Override
    public ContentLinks create(final List<String> contentUrls, final FormatDecoration formatDecoration) {
        return new ContentLinks(contentUrls.stream().map(ContentLink::new).collect(Collectors.toList()));
    }
}

