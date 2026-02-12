package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey;

class AccessTokenResourceFactory extends AbstractAccessTokenResourceFactory {

    @Override
    protected ContentProviderPropertyKey getOAuthUrlPropertyKey() {
        return ContentProviderPropertyKey.OAUTH_URL;
    }
}
