package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;

interface IContentProviderDataAccessorFactory {
    IContentProviderDataAccessor getInstance(ContentProvider contentProvider);
}
