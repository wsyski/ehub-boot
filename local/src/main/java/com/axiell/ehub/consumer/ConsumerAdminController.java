/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Default implementation of the {@link IConsumerAdminController}.
 */
public class ConsumerAdminController implements IConsumerAdminController {

    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;

    @Autowired
    private IContentProviderConsumerRepository contentProviderConsumerRepository;

    /**
     * @see com.axiell.ehub.consumer.IConsumerAdminController#delete(com.axiell.ehub.consumer.EhubConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(final EhubConsumer providedEhubConsumer) {
        EhubConsumer retrievedEhubConsumer = getEhubConsumer(providedEhubConsumer.getId());
        Set<ContentProviderConsumer> consumers = retrievedEhubConsumer.getContentProviderConsumers();
        contentProviderConsumerRepository.delete(consumers);
        ehubConsumerRepository.delete(retrievedEhubConsumer);
    }

    /**
     * @see com.axiell.ehub.consumer.IConsumerAdminController#delete(com.axiell.ehub.consumer.ContentProviderConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(ContentProviderConsumer contentProviderConsumer) {
        contentProviderConsumerRepository.delete(contentProviderConsumer);
    }

    /**
     * @see com.axiell.ehub.consumer.IConsumerAdminController#getEhubConsumer(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(final Long ehubConsumerId) {
        EhubConsumer ehubConsumer = ehubConsumerRepository.findOne(ehubConsumerId);
        return initialize(ehubConsumer);
    }

    /**
     * @see com.axiell.ehub.consumer.IConsumerAdminController#getEhubConsumers()
     */
    @Override
    @Transactional(readOnly = true)
    public List<EhubConsumer> getEhubConsumers() {
        return ehubConsumerRepository.findAllOrderedByDescription();
    }

    /**
     * @see com.axiell.ehub.consumer.IConsumerAdminController#save(com.axiell.ehub.consumer.EhubConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public EhubConsumer save(EhubConsumer ehubConsumer) {
        return ehubConsumerRepository.save(ehubConsumer);
    }

    /**
     * @see com.axiell.ehub.consumer.IConsumerAdminController#save(com.axiell.ehub.consumer.ContentProviderConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public ContentProviderConsumer save(ContentProviderConsumer contentProviderConsumer) {
        return contentProviderConsumerRepository.save(contentProviderConsumer);
    }

    @Override
    @Transactional(readOnly = false)
    public ContentProviderConsumer add(final Long ehubConsumerId, final ContentProviderConsumer providedContentProviderConsumer) {
        final EhubConsumer ehubConsumer = getEhubConsumer(ehubConsumerId);
        providedContentProviderConsumer.setEhubConsumer(ehubConsumer);
        final ContentProviderConsumer savedContentProviderConsumer = save(providedContentProviderConsumer);

        final Set<ContentProviderConsumer> contentProviderConsumers = ehubConsumer.getContentProviderConsumers();
        contentProviderConsumers.add(savedContentProviderConsumer);
        save(ehubConsumer);

        return savedContentProviderConsumer;
    }

    /**
     * Initializes the {@link EhubConsumer}, which includes initializing all its
     * {@link ContentProviderConsumer}s and all its properties.
     *
     * @param ehubConsumer the {@link EhubConsumer} to initialize
     * @return a completely initialized {@link EhubConsumer}
     */
    private EhubConsumer initialize(EhubConsumer ehubConsumer) {
        for (ContentProviderConsumer contentProviderConsumer : ehubConsumer.getContentProviderConsumers()) {
            initialize(contentProviderConsumer);
        }
        Hibernate.initialize(ehubConsumer.getProperties());
        return ehubConsumer;
    }

    /**
     * Initializes the {@link ContentProviderConsumer}, which includes
     * initializing all its properties.
     *
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to initialize
     * @return a completely initialized {@link ContentProviderConsumer}
     */
    private ContentProviderConsumer initialize(ContentProviderConsumer contentProviderConsumer) {
        Hibernate.initialize(contentProviderConsumer);
        Hibernate.initialize(contentProviderConsumer.getProperties());
        return contentProviderConsumer;
    }
}
