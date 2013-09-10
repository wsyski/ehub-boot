/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.axiell.ehub.ErrorCause;

/**
 * Provides the possibility to convert a string to an {@link AuthInfo} and an
 * {@link AuthInfo} to a string.
 */
@Provider
final class AuthInfoConverter implements StringConverter<AuthInfo> {

    @Autowired(required = true)
    private ISignatureFactory signatureFactory;

    @Override
    public AuthInfo fromString(final String authorizationHeader) {
	final AuthHeaderParser parser = new AuthHeaderParser(authorizationHeader);
	final Long ehubConsumerId = parser.getEhubConsumerId();	
	final String libraryCard = parser.getLibraryCard();
	final String pin = parser.getPin();
	final Signature expectedSignature = signatureFactory.createExpectedSignature(ehubConsumerId, libraryCard, pin);
	final Signature actualSignature = parser.getActualSignature();

	if (actualSignature.isValid(expectedSignature)) {
	    return new AuthInfo(ehubConsumerId, libraryCard, pin, actualSignature);
	} else {
	    throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
	}
    }

    @Override
    public String toString(final AuthInfo authInfo) {
	return authInfo.toString();
    }
}
