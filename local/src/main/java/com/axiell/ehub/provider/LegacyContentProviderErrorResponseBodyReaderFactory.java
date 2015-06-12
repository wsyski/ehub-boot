package com.axiell.ehub.provider;

import com.axiell.ehub.provider.overdrive.LegacyOverdriveErrorResponseBodyReaderLegacy;
import com.axiell.ehub.provider.elib.library3.LegacyElib3ErrorResponseBodyReader;

class LegacyContentProviderErrorResponseBodyReaderFactory {

    private LegacyContentProviderErrorResponseBodyReaderFactory() {
    }

    static ILegacyContentProviderErrorResponseBodyReader create(final ContentProvider contentProvider) {
        if (contentProvider == null)
            return new LegacyDefaultContentProviderErrorResponseBodyReader();

        final ContentProviderName name = contentProvider.getName();

        switch (name) {
            case OVERDRIVE:
                return new LegacyOverdriveErrorResponseBodyReaderLegacy();
            case ELIB3:
                return new LegacyElib3ErrorResponseBodyReader();
            default:
                return new LegacyDefaultContentProviderErrorResponseBodyReader();
        }
    }
}
