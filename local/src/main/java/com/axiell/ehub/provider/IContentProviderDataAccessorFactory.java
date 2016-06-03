package com.axiell.ehub.provider;

interface IContentProviderDataAccessorFactory {
    IContentProviderDataAccessor getInstance(ContentProvider contentProvider);
}
