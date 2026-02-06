package com.axiell.ehub.security;

import com.axiell.authinfo.InvalidAuthorizationHeaderSignatureRuntimeException;
import com.axiell.authinfo.MissingOrUnparseableAuthorizationHeaderRuntimeException;
import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static org.hamcrest.MatcherAssert.assertThat;

public class EhubAuthHeaderParser_ParseTest extends EhubAuthHeaderParserFixture {

    @Test
    public void missingAuthorizationHeader() {
        givenNoAuthorizationHeader();
        Exception exception = Assertions.assertThrows(MissingOrUnparseableAuthorizationHeaderRuntimeException.class, () -> {
            whenParse();
        });
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
            Assertions.fail("An UnauthorizedException should have been thrown");
        } catch (UnauthorizedException e) {
            thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
    }

    @Test
    public void missingLibraryCard() {
        givenNewAuthHeaderParserWithAuthorizationHeaderWithoutLibraryCard();
        whenParse();
        thenActualLibraryCardIsNull();
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
        Exception exception = Assertions.assertThrows(InvalidAuthorizationHeaderSignatureRuntimeException.class, () -> {
            whenParse();
        });

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
        Assertions.assertEquals(expectedErrorCause, actualErrorCause);
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
        Assertions.assertNull(authInfo.getPatron().getPin());
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
        Assertions.assertEquals(EHUB_CONSUMER_ID, authInfo.getEhubConsumerId());
    }


    private void thenActualLibraryCardEqualsExpectedLibraryCard() {
        Assertions.assertEquals(LIBRARY_CARD, authInfo.getPatron().getLibraryCard());
    }

    private void thenActualLibraryCardIsNull() {
        assertThat(authInfo.getPatron().getLibraryCard(), Matchers.nullValue());
    }

    private void thenActualPinEqualsExpectedPin() {
        Assertions.assertEquals(PIN, authInfo.getPatron().getPin());
    }

    private void thenActualEmailEqualsExpectedEmail() {
        Assertions.assertEquals(EMAIL, authInfo.getPatron().getEmail());
    }


    private void thenActualPatronIdEqualsExpectedPatronId() {
        Assertions.assertEquals(PATRON_ID, authInfo.getPatron().getId());
    }

    private void givenNewAuthHeaderParserWithValidAuthorizationHeaderIncludingPatronId() {
        authorizationHeader = MessageFormat
                .format("ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                        String.valueOf(EHUB_CONSUMER_ID), PATRON_ID, LIBRARY_CARD, PIN, SIGNATURE);
    }

    private void thenExpectedGeneratedPatronId() {
        Assertions.assertEquals("f8a323a1abfdcc3479ab60a9b6a0557a94a3527cd35a15b5735ebcbf59fe019c66b7d3bacb24b276b775c53e3cdd70bca88e705206979141c6c6fd8160dcd9b8",
                authInfo.getPatron().getId());
    }
}
