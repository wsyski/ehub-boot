package com.axiell.ehub.mock;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;

public class AbstractBusinessController {
    protected ContentProviderConsumer getContentProviderConsumer(final String contentProviderName) {
        ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setName(contentProviderName);
        contentProviderConsumer.setContentProvider(contentProvider);
        return contentProviderConsumer;
    }
}
