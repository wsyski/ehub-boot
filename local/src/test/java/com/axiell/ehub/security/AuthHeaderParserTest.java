package com.axiell.ehub.security;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Test;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;

public class AuthHeaderParserTest {
    private static final Long EXP_EHUB_CONSUMER_ID = 1L;
    private static final String EXP_CARD = "card";
    private static final String EXP_PIN = "pin";
    private static final Signature EXP_SIGNATURE = new Signature("c2lnbmF0dXJl");
    private AuthHeaderParser underTest;
    private String authorizationHeader;
    private Object actualReturnValue;
    
    @Test
    public void missingAuthorizationHeader() {
	givenNoAuthorizationHeader();
	try {
	    whenNewAuthHeaderParser();
	    Assert.fail("An UnauthorizedException should have been thrown");
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_AUTHORIZATION_HEADER);
	}
    }

    private void givenNoAuthorizationHeader() {
	authorizationHeader = null;
    }

    private void whenNewAuthHeaderParser() {
	underTest = new AuthHeaderParser(authorizationHeader);
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
    public void missingEhubConsumerId() {
	givenNewAuthHeaderParserWithAuthorizationHeaderWithoutEhubConsumerId();
	try {
	    whenGetEhubConsumerId();
	    Assert.fail("An UnauthorizedException should have been thrown");
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_EHUB_CONSUMER_ID);
	}
    }

    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutEhubConsumerId() {
	authorizationHeader = MessageFormat.format("eHUB ehub_library_card=\"{0}\", ehub_pin=\"{1}\", ehub_signature=\"{2}\"", EXP_CARD, EXP_PIN, EXP_SIGNATURE);
	underTest = new AuthHeaderParser(authorizationHeader);
    }
    
    private void whenGetEhubConsumerId() {
	actualReturnValue = underTest.getEhubConsumerId();
    }
    
    @Test
    public void missingLibraryCard() {
	givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard();
	whenGetLibraryCard();
	thenActualReturnValueIsNull();
    }
    
    private void thenActualReturnValueIsNull() {
	Assert.assertNull(actualReturnValue);
    }

    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard() {
	authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_pin=\"{1}\", ehub_signature=\"{2}\"", EXP_EHUB_CONSUMER_ID, EXP_PIN, EXP_SIGNATURE);
	underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void whenGetLibraryCard() {
	actualReturnValue = underTest.getLibraryCard();
    }
    
    @Test
    public void missingPin() {
	givenNewAuthHeaderParserWithAuthorizationHeaderWithoutPin();
	whenGetPin();
	thenActualReturnValueIsNull();
    }
    
    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutPin() {
	authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_signature=\"{2}\"", EXP_EHUB_CONSUMER_ID, EXP_CARD, EXP_SIGNATURE);
	underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void whenGetPin() {
	actualReturnValue = underTest.getPin();
    }
    
    @Test
    public void missingSignature() {
	givenNewAuthHeaderParserWithAuthorizationHeaderWithoutSignature();
	try {
	    whenGetSignature();
	    Assert.fail("An UnauthorizedException should have been thrown");
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_SIGNATURE);
	}
    }
    
    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutSignature() {
	authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\"", EXP_EHUB_CONSUMER_ID, EXP_CARD, EXP_PIN);
	underTest = new AuthHeaderParser(authorizationHeader);
    }
    
    private void whenGetSignature() {
	actualReturnValue = underTest.getActualSignature();
    }
    
    @Test
    public void getEhubConsumerId() {
	givenNewAuthHeaderParserWithValidAuthorizationHeader();
	whenGetEhubConsumerId();
	thenActualEhubConsumerIdEqualsExpectedEhubConsumerId();
    }

    private void givenNewAuthHeaderParserWithValidAuthorizationHeader() {
	authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_signature=\"{3}\"", EXP_EHUB_CONSUMER_ID, EXP_CARD, EXP_PIN, EXP_SIGNATURE);
	underTest = new AuthHeaderParser(authorizationHeader);
    }
    
    private void thenActualEhubConsumerIdEqualsExpectedEhubConsumerId() {
	Assert.assertEquals(EXP_EHUB_CONSUMER_ID, actualReturnValue);
    }
    
    @Test
    public void getLibraryCard() {
	givenNewAuthHeaderParserWithValidAuthorizationHeader();
	whenGetLibraryCard();
	thenActualLibraryCardEqualsExpectedLibraryCard();
    }
    
    private void thenActualLibraryCardEqualsExpectedLibraryCard() {
	Assert.assertEquals(EXP_CARD, actualReturnValue);
    }
    
    @Test
    public void getPin() {
	givenNewAuthHeaderParserWithValidAuthorizationHeader();
	whenGetPin();
	thenActualPinEqualsExpectedPin();
    }
    
    private void thenActualPinEqualsExpectedPin() {
	Assert.assertEquals(EXP_PIN, actualReturnValue);
    }
    
    @Test
    public void getSignature() {
	givenNewAuthHeaderParserWithValidAuthorizationHeader();
	whenGetSignature();
	thenActualSignatureEqualsExpectedSignature();
    }
    
    private void thenActualSignatureEqualsExpectedSignature() {
	Assert.assertEquals(EXP_SIGNATURE.toString(), actualReturnValue.toString());
    }
}
