package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;

interface IOcdFormatHandler {
    String getContentProviderFormat(ContentProviderConsumer contentProviderConsumer, String contentProviderAlias, String contentProviderRecordId);
}
