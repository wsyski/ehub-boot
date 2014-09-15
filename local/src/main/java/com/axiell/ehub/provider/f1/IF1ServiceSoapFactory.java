package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import se.forlagett.api.F1Service;
import se.forlagett.api.F1ServiceSoap;

interface IF1ServiceSoapFactory {

    F1ServiceSoap getInstance(ContentProviderConsumer contentProviderConsumer);
}
