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
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoConverterTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private static final String SECRET_KEY = "secret";
    private static final String CARD = "libraryCard";
    private static final String PIN = "pin";

    private AuthInfoConverter underTest;
    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private EhubConsumer ehubConsumer;
    private String authorizationHeader;
    private AuthInfo actualAuthInfo;

    @Before
    public void setUpAuthInfoConverter() {
	underTest = new AuthInfoConverter();
	ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
    }

    @Test
    public void validSignature() {
	givenValidSignatureInHeader();
	givenExpectedEhubConsumer();
	givenExpectedSecretKey();
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
    
    private void givenExpectedEhubConsumer() {
	given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(ehubConsumer);
    }

    private void givenExpectedSecretKey() {
	given(ehubConsumer.getSecretKey()).willReturn(SECRET_KEY);
    }

    private void whenFromString() {
	actualAuthInfo = underTest.fromString(authorizationHeader);
    }

    private void thenAuthInfoIsNotNull() {
	Assert.assertNotNull(actualAuthInfo);
    }

    @Test
    public void ehubConsumerNotFound() {
	givenValidSignatureInHeader();
	givenNoEhubConsumer();
	try {
	    whenFromString();
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.EHUB_CONSUMER_NOT_FOUND);
	}
    }

    private void givenNoEhubConsumer() {
	given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(null);
    }

    private void thenActualErrorCauseEqualsExpectedErrorCause(UnauthorizedException e, ErrorCause expectedErrorCause) {
	ErrorCause actualErrorCause = getActualErrorCause(e);
	Assert.assertEquals(expectedErrorCause, actualErrorCause);
    }

    private ErrorCause getActualErrorCause(UnauthorizedException e) {
	EhubError ehubError = e.getEhubError();
	return ehubError.getCause();
    }

    @Test
    public void missingSecretKey() {
	givenValidSignatureInHeader();
	givenExpectedEhubConsumer();
	givenNoSecretKey();
	try {
	    whenFromString();
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_SECRET_KEY);
	}
    }

    private void givenNoSecretKey() {
	given(ehubConsumer.getSecretKey()).willReturn(null);
    }

    @Test
    public void invalidSignature() {
	givenInvalidSignatureInHeader();
	givenExpectedEhubConsumer();
	givenExpectedSecretKey();
	try {
	    whenFromString();
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.INVALID_SIGNATURE);
	}
    }

    private void givenInvalidSignatureInHeader() {
	makeAuthorizationHeader("invalidSignature");
    }
}
