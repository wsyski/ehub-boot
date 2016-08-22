package com.axiell.ehub;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;

public class AbstractBusinessController {
    protected ContentProviderConsumer getContentProviderConsumer(final String contentProviderName) {
        ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setName(contentProviderName);
        contentProviderConsumer.setContentProvider(contentProvider);
        return contentProviderConsumer;
    }
}
