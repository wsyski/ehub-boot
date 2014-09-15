package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;

interface IF1SoapServiceParameterHelper {

    int getMediaId(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language);

    int getRegionId(ContentProviderConsumer contentProviderConsumer, String language);

    int getTypeId(ContentProviderConsumer contentProviderConsumer, String formatId, String language);

    int getLoanId(ContentProviderConsumer contentProviderConsumer, String loanId, String language);
}
