/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link IConsumerBusinessController}.
 */
public class ConsumerBusinessController implements IConsumerBusinessController {    
    @Autowired(required = true)
    private IEhubConsumerRepository ehubConsumerRepository;
    
    /**
     * @see com.axiell.ehub.consumer.IConsumerBusinessController#getEhubConsumer(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(Long ehubConsumerId) {
        return ehubConsumerRepository.findOne(ehubConsumerId);
    }
}
