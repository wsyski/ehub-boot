package com.axiell.ehub.provider.elib.elibu;

import com.axiell.ehub.consumer.ContentProviderConsumer;

interface IElibUFacade {
    
    Response getProduct(ContentProviderConsumer contentProviderConsumer, String elibuRecordId);

    Response consumeLicense(ContentProviderConsumer contentProviderConsumer, String libraryCard);

    Response consumeProduct(ContentProviderConsumer contentProviderConsumer, Integer licenseId, String elibuRecordId);
}
