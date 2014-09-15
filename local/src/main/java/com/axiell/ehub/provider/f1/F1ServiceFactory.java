package com.axiell.ehub.provider.f1;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.springframework.stereotype.Component;
import se.forlagett.api.F1Service;

import java.net.MalformedURLException;
import java.net.URL;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

@Component
class F1ServiceFactory implements IF1ServiceFactory {

    @Override
    public F1Service create(final ContentProviderConsumer contentProviderConsumer) {
        final URL wsdlUrl = getWsdlUrl(contentProviderConsumer);
        return new F1Service(wsdlUrl);
    }

    private URL getWsdlUrl(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String apiBaselUrlProperty = contentProvider.getProperty(API_BASE_URL);
        try {
            return new URL(apiBaselUrlProperty);
        } catch (MalformedURLException e) {
            throw new InternalServerErrorException("Could not get WSDL URL", e);
        }
    }
}
