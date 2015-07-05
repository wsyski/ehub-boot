package com.axiell.ehub.provider;

interface IContentProviderDataAccessorFactory {
    IContentProviderDataAccessor getInstance(String contentProviderName);
}
