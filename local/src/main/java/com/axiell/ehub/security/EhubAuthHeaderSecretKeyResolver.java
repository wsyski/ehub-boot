package com.axiell.ehub.security;

import com.axiell.auth.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class EhubAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {

    private boolean isValidate;
    private long expirationTimeInSeconds;

    private IConsumerBusinessController consumerBusinessController;

    @Transactional(readOnly = true)
    @Override
    public String getSecretKey(Long ehubConsumerId) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        return ehubConsumer.getSecretKey();
    }



    @Override
    public boolean isValidate() {
        return false;
    }

    @Override
    public long getExpirationTimeInSeconds() {
        return expirationTimeInSeconds;
    }

    @Required
    public void setConsumerBusinessController(final IConsumerBusinessController consumerBusinessController) {
        this.consumerBusinessController = consumerBusinessController;
    }

    @Required
    public void setValidate(final boolean isValidate) {
        this.isValidate = isValidate;
    }

    @Required
    public void setExpirationTimeInSeconds(final long expirationTimeInSeconds) {
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }
}
