/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of the {@link IContentProviderAdminController}.
 */
public class ContentProviderAdminController implements IContentProviderAdminController {
    @Autowired
    private IContentProviderRepository contentProviderRepository;

    /**
     * @see com.axiell.ehub.provider.IContentProviderAdminController#getContentProviders()
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContentProvider> getContentProviders() {
        return contentProviderRepository.findAllOrderedByName();
    }

    @Override
    @Transactional(readOnly = true)
    public ContentProvider getContentProvider(final String contentProviderName) {
        ContentProvider contentProvider = contentProviderRepository.findByName(contentProviderName);
        return initialize(contentProvider);
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderAdminController#save(com.axiell.ehub.provider.ContentProvider)
     */
    @Override
    @Transactional(readOnly = false)
    public ContentProvider save(ContentProvider contentProvider) {
        return contentProviderRepository.save(contentProvider);
    }

    /**
     * Initializes the {@link ContentProvider}, which includes initializing all its FormatDecorations and
     * all its properties.
     *
     * @param contentProvider the {@link ContentProvider} to initialize
     * @return a completely initialized {@link ContentProvider}
     */
    private ContentProvider initialize(ContentProvider contentProvider) {
        Hibernate.initialize(contentProvider);
        Hibernate.initialize(contentProvider.getProperties());
        Hibernate.initialize(contentProvider.getFormatDecorations());
        return contentProvider;
    }
}
