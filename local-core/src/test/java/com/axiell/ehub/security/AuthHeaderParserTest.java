package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static org.junit.Assert.assertEquals;

public class AuthHeaderParserTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private static final String PATRON_ID = "patronId";
    private static final String LIBRARY_CARD = "card";
    private static final String PIN = "pin";
    private static final String EMAIL = "arena@axiell.com";
    private static final String SIGNATURE = "signature";
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

    @Test
    public void getLibraryCard() {
        givenNewAuthHeaderParserWithValidAuthorizationHeader();
        whenGetLibraryCard();
        thenActualLibraryCardEqualsExpectedLibraryCard();
    }

    @Test
    public void getPin() {
        givenNewAuthHeaderParserWithValidAuthorizationHeader();
        whenGetPin();
        thenActualPinEqualsExpectedPin();
    }

    @Test
    public void getEmail() {
        givenNewAuthHeaderParserWithValidAuthorizationHeader();
        whenGetEmail();
        thenActualEmailEqualsExpectedEmail();
    }

    @Test
    public void getSignature() {
        givenNewAuthHeaderParserWithValidAuthorizationHeader();
        whenGetSignature();
        thenActualSignatureEqualsExpectedSignature();
    }

    private void thenActualSignatureEqualsExpectedSignature() {
        assertEquals(SIGNATURE.toString(), actualReturnValue.toString());
    }

    @Test
    public void getPatronId() {
        givenNewAuthHeaderParserWithValidAuthorizationHeaderIncludingPatronId();
        whenGetPatronId();
        thenActualPatronIdEqualsExpectedPatronId();
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

    @Test
    public void missingLibraryCard() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard();
        whenGetLibraryCard();
        thenActualReturnValueIsNull();
    }

    @Test
    public void missingPin() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutPin();
        whenGetPin();
        thenActualReturnValueIsNull();
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

    @Test
    public void getEhubConsumerId() {
        givenNewAuthHeaderParserWithValidAuthorizationHeader();
        whenGetEhubConsumerId();
        thenActualEhubConsumerIdEqualsExpectedEhubConsumerId();
    }

    private void givenNoAuthorizationHeader() {
        authorizationHeader = null;
    }

    private void whenNewAuthHeaderParser() {
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void thenActualErrorCauseEqualsExpectedErrorCause(UnauthorizedException e, ErrorCause expectedErrorCause) {
        ErrorCause actualErrorCause = getActualErrorCause(e);
        assertEquals(expectedErrorCause, actualErrorCause);
    }

    private ErrorCause getActualErrorCause(UnauthorizedException e) {
        EhubError ehubError = e.getEhubError();
        return ehubError.getCause();
    }


    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutEhubConsumerId() {
        authorizationHeader = MessageFormat.format("eHUB ehub_library_card=\"{0}\", ehub_pin=\"{1}\", ehub_signature=\"{2}\"", LIBRARY_CARD, PIN, SIGNATURE);
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void whenGetEhubConsumerId() {
        actualReturnValue = underTest.getEhubConsumerId();
    }


    private void thenActualReturnValueIsNull() {
        Assert.assertNull(actualReturnValue);
    }

    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard() {
        authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_pin=\"{1}\", ehub_signature=\"{2}\"", EHUB_CONSUMER_ID, PIN, SIGNATURE);
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void whenGetLibraryCard() {
        actualReturnValue = underTest.getLibraryCard();
    }


    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutPin() {
        authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_signature=\"{2}\"", EHUB_CONSUMER_ID,
                LIBRARY_CARD, SIGNATURE);
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void whenGetPin() {
        actualReturnValue = underTest.getPin();
    }

    private void whenGetEmail() {
        actualReturnValue = underTest.getEmail();
    }

    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutSignature() {
        authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\"", EHUB_CONSUMER_ID, LIBRARY_CARD,
                PIN);
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void whenGetSignature() {
        actualReturnValue = underTest.getActualSignature();
    }


    private void givenNewAuthHeaderParserWithValidAuthorizationHeader() {
        authorizationHeader =
                MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_email=\"{3}\", ehub_signature=\"{4}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN), authInfoEncode(EMAIL), SIGNATURE);
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    private void thenActualEhubConsumerIdEqualsExpectedEhubConsumerId() {
        assertEquals(EHUB_CONSUMER_ID, actualReturnValue);
    }


    private void thenActualLibraryCardEqualsExpectedLibraryCard() {
        assertEquals(LIBRARY_CARD, actualReturnValue);
    }


    private void thenActualPinEqualsExpectedPin() {
        assertEquals(PIN, actualReturnValue);
    }

    private void thenActualEmailEqualsExpectedEmail() {
        assertEquals(EMAIL, actualReturnValue);
    }

    private void whenGetPatronId() {
        actualReturnValue = underTest.getPatronId();
    }

    private void thenActualPatronIdEqualsExpectedPatronId() {
        assertEquals(PATRON_ID, actualReturnValue);
    }

    private void givenNewAuthHeaderParserWithValidAuthorizationHeaderIncludingPatronId() {
        authorizationHeader = MessageFormat
                .format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                        EHUB_CONSUMER_ID, PATRON_ID, LIBRARY_CARD, PIN, SIGNATURE);
        underTest = new AuthHeaderParser(authorizationHeader);
    }

    @Test
    public void missingPatronId() {
        givenNewAuthHeaderParserWithValidAuthorizationHeader();
        whenGetPatronId();
        thenActualReturnValueIsNull();
    }
}
