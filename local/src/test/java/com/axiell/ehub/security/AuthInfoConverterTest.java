/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.consumer.EhubConsumer;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoConverterTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private static final String SECRET_KEY = "secret";
    private static final String CARD = "libraryCard";
    private static final String PIN = "pin";

    private AuthInfoConverter underTest;
    @Mock
    private ISignatureFactory signatureFactory;
    @Mock
    private EhubConsumer ehubConsumer;
    private Signature expectedSignature;
    private String authorizationHeader;
    private AuthInfo actualAuthInfo;

    @Before
    public void setUpAuthInfoConverter() {
	underTest = new AuthInfoConverter();
	ReflectionTestUtils.setField(underTest, "signatureFactory", signatureFactory);
    }
    
    @Before
    public void setUpExpectedSignature() {
	expectedSignature = new Signature(EHUB_CONSUMER_ID, SECRET_KEY, CARD, PIN);
    }

    @Test
    public void validSignature() {
	givenValidSignatureInHeader();
	givenExpectedSignature();
	whenFromString();
	thenAuthInfoIsNotNull();
    }

    private void givenValidSignatureInHeader() {
	final Signature expSignature = new Signature(EHUB_CONSUMER_ID, SECRET_KEY, CARD, PIN);
	final String expEncodedSignature = expSignature.toString();
	makeAuthorizationHeader(expEncodedSignature);
    }

    private void makeAuthorizationHeader(String signature) {
	authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_signature=\"{3}\"",
		EHUB_CONSUMER_ID, CARD, PIN, signature);
    }
    
    private void givenExpectedSignature() {
	given(signatureFactory.createExpectedSignature(any(Long.class), any(String.class), any(String.class))).willReturn(expectedSignature);
    }

    private void whenFromString() {
	actualAuthInfo = underTest.fromString(authorizationHeader);
    }

    private void thenAuthInfoIsNotNull() {
	Assert.assertNotNull(actualAuthInfo);
    }
    
    @Test
    public void invalidSignature() {
	givenInvalidSignatureInHeader();
	givenExpectedSignature();
	try {
	    whenFromString();
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.INVALID_SIGNATURE);
	}
    }

    private void givenInvalidSignatureInHeader() {
	makeAuthorizationHeader("invalidSignature");
    }
    
    private void thenActualErrorCauseEqualsExpectedErrorCause(EhubRuntimeException e, ErrorCause expectedErrorCause) {
	ErrorCause actualErrorCause = getActualErrorCause(e);
	Assert.assertEquals(expectedErrorCause, actualErrorCause);
    }

    private ErrorCause getActualErrorCause(EhubRuntimeException e) {
	EhubError ehubError = e.getEhubError();
	return ehubError.getCause();
    }
}
