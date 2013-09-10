package com.axiell.ehub.provider;

interface IContentProviderDataAccessorFactory {
    IContentProviderDataAccessor getInstance(ContentProviderName contentProviderName);
}
