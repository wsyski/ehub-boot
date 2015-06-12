package com.axiell.ehub.provider;

class ContentProviderErrorResponseBodyReaderFactory {

    private ContentProviderErrorResponseBodyReaderFactory() {
    }

    static IContentProviderErrorResponseBodyReader create(final ContentProvider contentProvider) {
        if (contentProvider == null)
            return new DefaultContentProviderErrorResponseBodyReader();

        final ContentProviderName name = contentProvider.getName();

        switch (name) {
            default:
                return new DefaultContentProviderErrorResponseBodyReader();
        }
    }
}
