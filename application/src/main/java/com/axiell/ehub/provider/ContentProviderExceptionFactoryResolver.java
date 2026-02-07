package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.elib.library3.Elib3ExceptionFactory;
import com.axiell.ehub.provider.ep.EpExceptionFactory;
import com.axiell.ehub.provider.overdrive.OverDriveExceptionFactory;

class ContentProviderExceptionFactoryResolver {

    private ContentProviderExceptionFactoryResolver() {
    }

    static IContentProviderExceptionFactory create(final ContentProviderConsumer contentProviderConsumer, final String language,
                                                   final IEhubExceptionFactory ehubExceptionFactory) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String contentProviderName = contentProvider.getName();
        if (ContentProvider.CONTENT_PROVIDER_ELIB3.equals(contentProviderName)) {
            return new Elib3ExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_OVERDRIVE.equals(contentProviderName)) {
            return new OverDriveExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else {
            return new EpExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        }
    }
}
