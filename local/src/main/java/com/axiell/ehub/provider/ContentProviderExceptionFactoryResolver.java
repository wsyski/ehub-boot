package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.askews.AskewsExceptionFactory;
import com.axiell.ehub.provider.borrowbox.BorrowBoxExceptionFactory;
import com.axiell.ehub.provider.elib.library3.Elib3ExceptionFactory;
import com.axiell.ehub.provider.ep.EpExceptionFactory;
import com.axiell.ehub.provider.ocd.OcdExceptionFactory;
import com.axiell.ehub.provider.overdrive.OverDriveExceptionFactory;
import com.axiell.ehub.provider.zinio.ZinioExceptionFactory;

class ContentProviderExceptionFactoryResolver {

    private ContentProviderExceptionFactoryResolver() {
    }

    static IContentProviderExceptionFactory create(final ContentProviderConsumer contentProviderConsumer, final String language,
                                                   final IEhubExceptionFactory ehubExceptionFactory) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String contentProviderName = contentProvider.getName();
        if (ContentProvider.CONTENT_PROVIDER_ELIBU.equals(contentProviderName) ||
                ContentProvider.CONTENT_PROVIDER_F1.equals(contentProviderName)) {
            return new DefaultContentProviderExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_ASKEWS.equals(contentProviderName)) {
            return new AskewsExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_ELIB3.equals(contentProviderName)) {
            return new Elib3ExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_OVERDRIVE.equals(contentProviderName)) {
            return new OverDriveExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_BORROWBOX.equals(contentProviderName)) {
            return new BorrowBoxExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_OCD.equals(contentProviderName)) {
            return new OcdExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else if (ContentProvider.CONTENT_PROVIDER_ZINIO.equals(contentProviderName)) {
            return new ZinioExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        } else {
            return new EpExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
        }
    }
}
