package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey;

class PatronAccessTokenResourceFactory extends AbstractAccessTokenResourceFactory {

    @Override
    protected ContentProviderPropertyKey getOAuthUrlPropertyKey() {
        return ContentProviderPropertyKey.OAUTH_PATRON_URL;
    }
}
