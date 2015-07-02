package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.borrowbox.BorrowBoxExceptionFactory;
import com.axiell.ehub.provider.elib.library3.Elib3ExceptionFactory;
import com.axiell.ehub.provider.epi.EpiExceptionFactory;
import com.axiell.ehub.provider.ocd.OcdExceptionFactory;
import com.axiell.ehub.provider.overdrive.OverDriveExceptionFactory;

class ContentProviderExceptionFactoryResolver {

    private ContentProviderExceptionFactoryResolver() {
    }

    static IContentProviderExceptionFactory create(final ContentProviderConsumer contentProviderConsumer, final String language,
                                                   final IEhubExceptionFactory ehubExceptionFactory) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final ContentProviderName name = contentProvider.getName();
        switch (name) {
            case OCD:
                return new OcdExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
            case BORROWBOX:
                return new BorrowBoxExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
            case ELIB3:
                return new Elib3ExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
            case OVERDRIVE:
                return new OverDriveExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
            case ELIB:
            case ELIBU:
            case PUBLIT:
            case ASKEWS:
            case F1:
                return new DefaultContentProviderExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
            default:
                return new EpiExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        }
    }
}
