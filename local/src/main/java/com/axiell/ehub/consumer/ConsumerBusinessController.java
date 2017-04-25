/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.auth.AuthInfo;
import com.axiell.ehub.security.UnauthorizedException;

/**
 * Default implementation of the {@link IConsumerBusinessController}.
 */
public class ConsumerBusinessController implements IConsumerBusinessController {    
    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;
    
    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(final Long ehubConsumerId) {
	final EhubConsumer ehubConsumer = ehubConsumerRepository.findOne(ehubConsumerId);
	
	if (ehubConsumer == null) {
	    final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
	    throw new UnauthorizedException(ErrorCause.EHUB_CONSUMER_NOT_FOUND, ehubConsumerIdArg);
	}
	return ehubConsumer;
    }
    
    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(final AuthInfo authInfo) {
	final Long ehubConsumerId = authInfo.getEhubConsumerId();
        return getEhubConsumer(ehubConsumerId);
    }
}
