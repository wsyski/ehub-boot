/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
public class ConsumerAdminController implements IConsumerAdminController {

    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;

    @Autowired
    private IContentProviderConsumerRepository contentProviderConsumerRepository;

    /**
     * @see IConsumerAdminController#delete(EhubConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(final EhubConsumer providedEhubConsumer) {
        EhubConsumer retrievedEhubConsumer = getEhubConsumer(providedEhubConsumer.getId());
        Set<ContentProviderConsumer> consumers = retrievedEhubConsumer.getContentProviderConsumers();
        contentProviderConsumerRepository.deleteAll(consumers);
        ehubConsumerRepository.delete(retrievedEhubConsumer);
    }

    /**
     * @see IConsumerAdminController#delete(ContentProviderConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(ContentProviderConsumer contentProviderConsumer) {
        contentProviderConsumerRepository.delete(contentProviderConsumer);
    }

    /**
     * @see IConsumerAdminController#getEhubConsumer(Long)
     */
    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(final Long ehubConsumerId) {
        return ehubConsumerRepository.findById(ehubConsumerId)
                .map(this::initialize)
                .orElse(null);
    }

    /**
     * @see IConsumerAdminController#getEhubConsumers()
     */
    @Override
    @Transactional(readOnly = true)
    public List<EhubConsumer> getEhubConsumers() {
        return ehubConsumerRepository.findAllOrderedByDescription();
    }

    /**
     * @see IConsumerAdminController#save(EhubConsumer)
     */
    @Override
    @Transactional(readOnly = false)
    public EhubConsumer save(EhubConsumer ehubConsumer) {
        return ehubConsumerRepository.save(ehubConsumer);
    }

    /**
     * @see IConsumerAdminController#save(ContentProviderConsumer)
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

    @Override
    @Transactional(readOnly = false)
    public void deleteByContentProviderId(final long contentProviderId) {
        contentProviderConsumerRepository.deleteByContentProviderId(contentProviderId);
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
