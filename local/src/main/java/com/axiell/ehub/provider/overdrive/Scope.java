package com.axiell.ehub.provider.overdrive;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDIRVE_WEBSITE_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_ILS_NAME;

import com.axiell.ehub.consumer.ContentProviderConsumer;

public final class Scope {
    private final String webSiteId;
    private final String ilsName;

    private Scope(final String webSiteId, final String ilsName) {
	this.webSiteId = webSiteId;
	this.ilsName = ilsName;
    }

    static Scope fromContentProviderConsumer(final ContentProviderConsumer contentProviderConsumer) {
	final String siteId = contentProviderConsumer.getProperty(OVERDIRVE_WEBSITE_ID);
	final String name = contentProviderConsumer.getProperty(OVERDRIVE_ILS_NAME);
	return new Scope(siteId, name);
    }

    @Override
    public String toString() {
        return "websiteid:" + webSiteId + " ilsname:" + ilsName;
    }
}
