package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.util.FinalWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.forlagett.api.F1Service;
import se.forlagett.api.F1ServiceSoap;

@Component
class F1ServiceSoap12Factory implements IF1ServiceSoapFactory {
    private FinalWrapper<F1ServiceSoap> f1ServiceSoapWrapper;

    @Autowired(required = true)
    private IF1ServiceFactory f1ServiceFactory;

    @Override
    public F1ServiceSoap getInstance(final ContentProviderConsumer contentProviderConsumer) {
        // See http://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
        FinalWrapper<F1ServiceSoap> wrapper = f1ServiceSoapWrapper;

        if (wrapper == null) {
            synchronized(this) {
                if (f1ServiceSoapWrapper == null)
                    f1ServiceSoapWrapper = createF1ServiceSoap12Wrapper(contentProviderConsumer);
                wrapper = f1ServiceSoapWrapper;
            }
        }
        return wrapper.value;
    }

    private FinalWrapper<F1ServiceSoap> createF1ServiceSoap12Wrapper(final ContentProviderConsumer contentProviderConsumer) {
        final F1Service f1Service = f1ServiceFactory.create(contentProviderConsumer);
        final F1ServiceSoap f1ServiceSoap = f1Service.getF1ServiceSoap12();
        return new FinalWrapper<>(f1ServiceSoap);
    }
}
