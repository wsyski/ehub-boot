package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

class PatronAccessTokenResourceFactory extends AbstractAccessTokenResourceFactory {

    @Override
    protected ContentProviderPropertyKey getOAuthUrlPropertyKey() {
        return ContentProviderPropertyKey.OAUTH_PATRON_URL;
    }
}
