package com.axiell.ehub.provider;

import com.axiell.ehub.provider.ocd.OcdErrorResponseBodyReader;

class ContentProviderErrorResponseBodyReaderFactory {

    private ContentProviderErrorResponseBodyReaderFactory() {
    }

    static IContentProviderErrorResponseBodyReader create(final ContentProvider contentProvider) {
        if (contentProvider == null)
            return new DefaultContentProviderErrorResponseBodyReader();

        final ContentProviderName name = contentProvider.getName();

        switch (name) {
            case OCD:
                return new OcdErrorResponseBodyReader();
            default:
                return new DefaultContentProviderErrorResponseBodyReader();
        }
    }
}
