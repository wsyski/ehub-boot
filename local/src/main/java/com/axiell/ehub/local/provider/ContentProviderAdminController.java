/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
import com.axiell.ehub.local.loan.ILoanAdminController;
import com.axiell.ehub.local.provider.alias.IAliasAdminController;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
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
     * @see IContentProviderAdminController#getContentProviders()
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContentProvider> getContentProviders() {
        return contentProviderRepository.findAllOrderedByName();
    }

    @Override
    @Transactional(readOnly = true)
    public ContentProvider getContentProvider(final String contentProviderName) {
        ContentProvider contentProvider = contentProviderRepository.findOneByName(contentProviderName);
        return initialize(contentProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsContentProviderName(final String contentProviderName) {
        final ContentProvider contentProvider = contentProviderRepository.findOneByName(contentProviderName);
        return contentProvider != null;
    }

    /**
     * @see IContentProviderAdminController#save(ContentProvider)
     */
    @Override
    @Transactional(readOnly = false)
    public ContentProvider save(final ContentProvider contentProvider) {
        return contentProviderRepository.save(contentProvider);
    }

    /**
     * @see IContentProviderAdminController#delete(ContentProvider)
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
        contentProvider.getFormatDecorations().values().forEach(formatDecoration -> Hibernate.initialize(formatDecoration.getPlatforms()));
        return contentProvider;
    }
}
