package com.axiell.ehub.security;

import com.axiell.auth.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class EhubAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {

    private boolean isValidateSignature;

    private IConsumerBusinessController consumerBusinessController;

    @Transactional(readOnly = true)
    @Override
    public String getSecretKey(Long ehubConsumerId) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        return ehubConsumer.getSecretKey();
    }

    @Override
    public boolean isValidateSignature() {
        return isValidateSignature;
    }

    @Required
    public void setValidateSignature(final boolean isValidateSignature) {
        this.isValidateSignature = isValidateSignature;
    }

    @Required
    public void setConsumerBusinessController(final IConsumerBusinessController consumerBusinessController) {
        this.consumerBusinessController = consumerBusinessController;
    }
}
