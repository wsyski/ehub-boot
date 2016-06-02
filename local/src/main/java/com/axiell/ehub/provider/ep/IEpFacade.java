package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

public interface IEpFacade {
    RecordDTO getRecord(ContentProviderConsumer contentProviderConsumer, Patron patron, String contentProviderRecordId);
}
