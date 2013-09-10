/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;

/**
 * Provides the possibility to convert a string to an {@link AuthInfo} and an
 * {@link AuthInfo} to a string.
 */
@Provider
final class AuthInfoConverter implements StringConverter<AuthInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInfoConverter.class);    

    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    /**
     * @see org.jboss.resteasy.spi.StringConverter#fromString(java.lang.String)
     */
    @Override
    public AuthInfo fromString(final String authorizationHeader) {
	final AuthHeaderParser parser = new AuthHeaderParser(authorizationHeader);
	final Long ehubConsumerId = parser.getEhubConsumerId();	
	final String libraryCard = parser.getLibraryCard();
	final String pin = parser.getPin();
	final Signature expectedSignature = makeExpectedSignature(ehubConsumerId, libraryCard, pin);
	final Signature actualSignature = parser.getActualSignature();
	logSignatures(expectedSignature, actualSignature);

	if (actualSignature.isValid(expectedSignature)) {
	    return new AuthInfo(ehubConsumerId, libraryCard, pin, actualSignature);
	} else {
	    throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
	}
    }    
    
    private Signature makeExpectedSignature(final Long ehubConsumerId, final String libraryCard, final String pin) {
	final String secretKey = getSecretKey(ehubConsumerId);
	return new Signature(ehubConsumerId, secretKey, libraryCard, pin);
    }

    private String getSecretKey(final Long ehubConsumerId) {
	final EhubConsumer ehubConsumer = getEhubConsumer(ehubConsumerId);
	final String secretKey = ehubConsumer.getSecretKey();

	if (secretKey == null) {
	    final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
	    throw new UnauthorizedException(ErrorCause.MISSING_SECRET_KEY, ehubConsumerIdArg);
	}
	return secretKey;
    }

    private EhubConsumer getEhubConsumer(final Long ehubConsumerId) {
	final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);

	if (ehubConsumer == null) {
	    final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
	    throw new UnauthorizedException(ErrorCause.EHUB_CONSUMER_NOT_FOUND, ehubConsumerIdArg);
	}
	return ehubConsumer;
    }

    private void logSignatures(final Signature expectedSignature, final Signature actualSignature) {
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("Expected signature = '" + expectedSignature.toString() + "'");
	    LOGGER.debug("Actual signature   = '" + actualSignature.toString() + "'");
	}
    }

    /**
     * @see org.jboss.resteasy.spi.StringConverter#toString(java.lang.Object)
     */
    @Override
    public String toString(final AuthInfo authInfo) {
	return authInfo.toString();
    }
}
