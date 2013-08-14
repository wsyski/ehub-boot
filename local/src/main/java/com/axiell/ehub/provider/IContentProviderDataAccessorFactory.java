package com.axiell.ehub.provider;

public interface IContentProviderDataAccessorFactory {
    public IContentProviderDataAccessor getInstance(ContentProviderName contentProviderName);
}
