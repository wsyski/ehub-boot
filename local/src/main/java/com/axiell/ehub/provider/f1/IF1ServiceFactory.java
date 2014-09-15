package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import se.forlagett.api.F1Service;

interface IF1ServiceFactory {

    F1Service create(ContentProviderConsumer contentProviderConsumer);
}
