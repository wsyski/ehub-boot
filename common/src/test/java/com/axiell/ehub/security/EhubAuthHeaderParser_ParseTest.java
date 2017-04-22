package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static org.junit.Assert.assertEquals;

public class EhubAuthHeaderParser_ParseTest extends EhubAuthHeaderParserFixture {

    @Test
    public void missingAuthorizationHeader() {
        givenNoAuthorizationHeader();
        try {
            whenParse();
            Assert.fail("An UnauthorizedException should have been thrown");
        } catch (UnauthorizedException e) {
            thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_AUTHORIZATION_HEADER);
        }
    }

    @Test
    public void getLibraryCard() {
        givenValidAuthorizationHeader();
        whenParse();
        thenActualLibraryCardEqualsExpectedLibraryCard();
    }

    @Test
    public void getPin() {
        givenValidAuthorizationHeader();
        whenParse();
        thenActualPinEqualsExpectedPin();
    }

    @Test
    public void getEmail() {
        givenValidAuthorizationHeader();
        whenParse();
        thenActualEmailEqualsExpectedEmail();
    }

    @Test
    public void getPatronId() {
        givenNewAuthHeaderParserWithValidAuthorizationHeaderIncludingPatronId();
        whenParse();
        thenActualPatronIdEqualsExpectedPatronId();
    }

    @Test
    public void missingEhubConsumerId() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutEhubConsumerId();
        try {
            whenParse();
            Assert.fail("An UnauthorizedException should have been thrown");
        } catch (UnauthorizedException e) {
            thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void missingLibraryCard() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard();
        whenParse();
        thenActualLibraryCardEqualsExpectedLibraryCard();
    }

    @Test
    public void missingPin() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutPin();
        whenParse();
        thenActualReturnValueIsNull();
    }

    @Test
    public void missingSignature() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutSignature();
        try {
            whenParse();
            Assert.fail("An UnauthorizedException should have been thrown");
        } catch (UnauthorizedException e) {
            thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_SIGNATURE);
        }
    }

    @Test
    public void getEhubConsumerId() {
        givenValidAuthorizationHeader();
        whenParse();
        thenActualEhubConsumerIdEqualsExpectedEhubConsumerId();
    }

    @Test
    public void missingPatronId() {
        givenValidAuthorizationHeader();
        whenParse();
        thenExpectedGeneratedPatronId();
    }

    private void givenNoAuthorizationHeader() {
        authorizationHeader = null;
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
        authorizationHeader = MessageFormat.format("ehub_library_card=\"{0}\", ehub_pin=\"{1}\", ehub_signature=\"{2}\"", LIBRARY_CARD, PIN, SIGNATURE);
    }

    private void whenParse() {
        authInfo = underTest.parse(authorizationHeader);
    }


    private void thenActualReturnValueIsNull() {
        Assert.assertNull(authInfo.getPatron().getPin());
    }

    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard() {
        authorizationHeader = MessageFormat.format("ehub_consumer_id=\"{0}\", ehub_pin=\"{1}\", ehub_signature=\"{2}\"", String.valueOf(EHUB_CONSUMER_ID), PIN, SIGNATURE);
    }


    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutPin() {
        authorizationHeader = MessageFormat.format("ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_signature=\"{2}\"", String.valueOf(EHUB_CONSUMER_ID),
                LIBRARY_CARD, SIGNATURE);
    }

    private void givenNewAuthHeaderParserWithAuthorizationHeaderWithoutSignature() {
        authorizationHeader = MessageFormat.format("ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\"", String.valueOf(EHUB_CONSUMER_ID), LIBRARY_CARD,
                PIN);
    }

    private void givenValidAuthorizationHeader() {
        authorizationHeader =
                MessageFormat.format("ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_email=\"{3}\", ehub_signature=\"{4}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN), authInfoEncode(EMAIL), SIGNATURE);
    }

    private void thenActualEhubConsumerIdEqualsExpectedEhubConsumerId() {
        assertEquals(EHUB_CONSUMER_ID, authInfo.getEhubConsumerId());
    }


    private void thenActualLibraryCardEqualsExpectedLibraryCard() {
        assertEquals(LIBRARY_CARD, authInfo.getPatron().getLibraryCard());
    }


    private void thenActualPinEqualsExpectedPin() {
        assertEquals(PIN, authInfo.getPatron().getPin());
    }

    private void thenActualEmailEqualsExpectedEmail() {
        assertEquals(EMAIL, authInfo.getPatron().getEmail());
    }


    private void thenActualPatronIdEqualsExpectedPatronId() {
        assertEquals(PATRON_ID, authInfo.getPatron().getId());
    }

    private void givenNewAuthHeaderParserWithValidAuthorizationHeaderIncludingPatronId() {
        authorizationHeader = MessageFormat
                .format("ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                        String.valueOf(EHUB_CONSUMER_ID), PATRON_ID, LIBRARY_CARD, PIN, SIGNATURE);
    }

    private void thenExpectedGeneratedPatronId() {
        Assert.assertEquals("f8a323a1abfdcc3479ab60a9b6a0557a94a3527cd35a15b5735ebcbf59fe019c66b7d3bacb24b276b775c53e3cdd70bca88e705206979141c6c6fd8160dcd9b8",
                authInfo.getPatron().getId());
    }
}
