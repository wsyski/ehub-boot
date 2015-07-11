/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.loan.ILoanAdminController;
import com.axiell.ehub.provider.alias.IAliasAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link IContentProviderAdminController}.
 */
public class ContentProviderAdminController implements IContentProviderAdminController {
    @Autowired
    private IContentProviderRepository contentProviderRepository;
    @Autowired
    private IConsumerAdminController consumerAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private ILoanAdminController loanAdminController;
    @Autowired
    private IAliasAdminController aliasAdminController;

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

    @Override
    @Transactional(readOnly = true)
    public boolean existsContentProviderName(final String contentProviderName) {
        final ContentProvider contentProvider = contentProviderRepository.findByName(contentProviderName);
        return contentProvider != null;
    }
    /**
     * @see com.axiell.ehub.provider.IContentProviderAdminController#save(com.axiell.ehub.provider.ContentProvider)
     */
    @Override
    @Transactional(readOnly = false)
    public ContentProvider save(final ContentProvider contentProvider) {
        return contentProviderRepository.save(contentProvider);
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderAdminController#delete(com.axiell.ehub.provider.ContentProvider)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(final ContentProvider providedContentProvider) {
        ContentProvider retrievedContentProvider = getContentProvider(providedContentProvider.getName());
        Map<String, FormatDecoration> formatDecorations = retrievedContentProvider.getFormatDecorations();
        for (FormatDecoration formatDecoration : formatDecorations.values()) {
            formatAdminController.delete(formatDecoration);
        }
        consumerAdminController.deleteByContentProviderId(retrievedContentProvider.getId());
        loanAdminController.deleteByContentProviderId(retrievedContentProvider.getId());
        aliasAdminController.deleteByContentProviderName(retrievedContentProvider.getName());
        contentProviderRepository.delete(retrievedContentProvider);
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
