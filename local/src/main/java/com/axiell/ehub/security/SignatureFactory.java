package com.axiell.ehub.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;

class SignatureFactory implements ISignatureFactory {
    
    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    @Override
    public Signature createExpectedSignature(final Long ehubConsumerId, final String libraryCard, final String pin) {
	final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
	final String secretKey = ehubConsumer.getSecretKey();
	return new Signature(ehubConsumerId, secretKey, libraryCard, pin);
    }
}
