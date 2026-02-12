/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.consumer;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.ErrorCauseArgument.Type;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.security.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConsumerBusinessController implements IConsumerBusinessController {
    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;

    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(final Long ehubConsumerId) {
        return ehubConsumerRepository.findById(ehubConsumerId)
                .orElseThrow(() -> {
                    final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
                    return new UnauthorizedException(ErrorCause.EHUB_CONSUMER_NOT_FOUND, ehubConsumerIdArg);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public EhubConsumer getEhubConsumer(final AuthInfo authInfo) {
        final Long ehubConsumerId = authInfo.getEhubConsumerId();
        return getEhubConsumer(ehubConsumerId);
    }
}
