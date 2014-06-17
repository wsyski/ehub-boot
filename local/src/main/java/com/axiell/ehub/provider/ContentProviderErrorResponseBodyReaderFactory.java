package com.axiell.ehub.provider;

import com.axiell.ehub.provider.overdrive.OverdriveErrorResponseBodyReader;
import com.axiell.ehub.provider.elib.library3.Elib3ErrorResponseBodyReader;

class ContentProviderErrorResponseBodyReaderFactory {

    static IContentProviderErrorResponseBodyReader create(final ContentProvider contentProvider) {
        if (contentProvider == null)
            return new DefaultContentProviderErrorResponseBodyReader();

        final ContentProviderName name = contentProvider.getName();

        switch (name) {
            case OVERDRIVE:
                return new OverdriveErrorResponseBodyReader();
            case ELIB3:
                return new Elib3ErrorResponseBodyReader();
            default:
                return new DefaultContentProviderErrorResponseBodyReader();
        }
    }
}
