package com.axiell.ehub.common.consumer;

import com.axiell.ehub.common.provider.ContentProvider;

import java.io.Serializable;
import java.util.Comparator;

class ContentProviderConsumerComparator implements Comparator<ContentProviderConsumer>, Serializable {

    @Override
    public int compare(final ContentProviderConsumer consumer1, final ContentProviderConsumer consumer2) {
        final ContentProvider provider1 = consumer1.getContentProvider();
        final ContentProvider provider2 = consumer2.getContentProvider();
        final String name1 = provider1.getName();
        final String name2 = provider2.getName();
        return name1.compareTo(name2);
    }
}
