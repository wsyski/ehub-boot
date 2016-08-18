package com.axiell.ehub.security;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class AuthInfoSecretKeyResolver implements IAuthInfoSecretKeyResolver {
    @Autowired
    private IConsumerBusinessController consumerBusinessController;

    @Transactional(readOnly = true)
    @Override
    public String getSecretKey(long ehubConsumerId) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        return ehubConsumer.getSecretKey();
    }
}
