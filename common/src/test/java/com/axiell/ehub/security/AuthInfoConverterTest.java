package com.axiell.ehub.security;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.AuthInfoConverter;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.InvalidAuthorizationHeaderSignatureRuntimeException;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.MessageFormat;
import java.util.Collections;

import static com.axiell.authinfo.IAuthHeaderParser.EHUB_SCHEME;
import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoConverterTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private static final String SECRET_KEY = "c2VjcmV0S2V5";
    private static final String PATRON_ID = "patronId";
    private static final String CARD = "libraryCard";
    private static final String PIN = "pin";
    private static final String EMAIL = "arena@axiell.com";

    private AuthInfoConverter underTest;
    @Mock
    private IAuthHeaderSecretKeyResolver authInfoSecretKeyResolver;
    private Patron patron;
    private String patronId;
    private String card;
    private String pin;
    private String email;
    private Signature expSignature;
    private String authorizationHeader;
    private EhubAuthHeaderParser ehubAuthHeaderParser;
    private AuthInfo actualAuthInfo;

    @Before
    public void setUpAuthInfoResolver() {
        given(authInfoSecretKeyResolver.getSecretKey(any())).willReturn(SECRET_KEY);
        given(authInfoSecretKeyResolver.isValidate()).willReturn(true);
        given(authInfoSecretKeyResolver.getExpirationTimeInSeconds(any())).willReturn(0L);
        ehubAuthHeaderParser = new EhubAuthHeaderParser(authInfoSecretKeyResolver);
        underTest = new AuthInfoConverter(Collections.singletonMap(EHUB_SCHEME, ehubAuthHeaderParser), EHUB_SCHEME);
    }

    @Test
    public void validSignature_withPatronId() throws EhubException {
        givenPatronId();
        givenCard();
        givenPin();
        givenPatron();
        givenValidSignature();
        givenAuthorizationHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
    }

    @Test
    public void validSignature_withPatronIdAndEmail() throws EhubException {
        givenPatronId();
        givenCard();
        givenPin();
        givenEmail();
        givenPatron();
        givenValidSignature();
        givenAuthorizationHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
    }

    private void givenCard() {
        card = CARD;
    }

    private void givenPin() {
        pin = PIN;
    }

    private void givenEmail() {
        email = EMAIL;
    }

    private void givenAuthorizationHeader() throws EhubException {
        AuthInfo authInfo = new AuthInfo.Builder().ehubConsumerId(EHUB_CONSUMER_ID).patron(patron).build();
        authorizationHeader = EHUB_SCHEME + " " + ehubAuthHeaderParser.serialize(authInfo);
    }

    private void givenValidSignature() {
        expSignature = new Signature(EhubAuthHeaderParser.getSignatureItems(EHUB_CONSUMER_ID, patron), SECRET_KEY);
    }

    private void givenPatron() {
        patron = new Patron.Builder().id(patronId).libraryCard(card).pin(pin).email(email).build();
    }

    private void givenPatronId() {
        patronId = PATRON_ID;
    }

    private void whenResolve() {
        actualAuthInfo = underTest.fromString(authorizationHeader);
    }

    private void thenAuthInfoIsNotNull() {
        Assert.assertNotNull(actualAuthInfo);
    }

    @Test
    public void validSignature_withoutPatronId() {
        givenCard();
        givenPin();
        givenPatron();
        givenValidSignature();
        givenLegacyAuthorizationHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
    }

    private void givenLegacyAuthorizationHeader() {
        makeAuthorizationHeaderWithoutPatronIdButWithCardPin(expSignature.toString());
    }

    @Test(expected = InvalidAuthorizationHeaderSignatureRuntimeException.class)
    public void invalidSignature() {
        givenInvalidSignatureInHeader();
        whenResolve();
    }

    private void givenInvalidSignatureInHeader() {
        makeAuthorizationHeaderWithoutPatronIdButWithCardPin("invalidSignature");
    }

    private void makeAuthorizationHeaderWithoutPatronIdButWithCardPin(String signature) {
        authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_signature=\"{3}\"",
                EHUB_CONSUMER_ID, authInfoEncode(CARD), authInfoEncode(PIN), authInfoEncode(signature));
    }

    private ErrorCause getActualErrorCause(EhubRuntimeException e) {
        EhubError ehubError = e.getEhubError();
        return ehubError.getCause();
    }

    @Test
    public void validSignature_withoutPatronIdCardPin() throws EhubException {
        givenPatron();
        givenValidSignature();
        givenAuthorizationHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
    }
}
