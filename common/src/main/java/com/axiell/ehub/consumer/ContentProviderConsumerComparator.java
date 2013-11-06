package com.axiell.ehub.consumer;

import java.io.Serializable;
import java.util.Comparator;

import com.axiell.ehub.provider.ContentProvider;

class ContentProviderConsumerComparator implements Comparator<ContentProviderConsumer>, Serializable {

    @Override
    public int compare(final ContentProviderConsumer consumer1, final ContentProviderConsumer consumer2) {
	final ContentProvider provider1 = consumer1.getContentProvider();
	final ContentProvider provider2 = consumer2.getContentProvider();
	final String name1 = provider1.getName().toString();
	final String name2 = provider2.getName().toString();
	return name1.compareTo(name2);
    }
}
