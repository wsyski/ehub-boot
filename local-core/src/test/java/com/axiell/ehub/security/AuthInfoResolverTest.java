package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.patron.Patron;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.MessageFormat;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoResolverTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private static final String SECRET_KEY = "c2VjcmV0S2V5";
    private static final String PATRON_ID = "patronId";
    private static final String CARD = "libraryCard";
    private static final String PIN = "pin";
    private static final String EMAIL = "arena@axiell.com";

    private AuthInfoResolver underTest;
    @Mock
    private IAuthInfoSecretKeyResolver authInfoSecretKeyResolver;
    private Patron patron;
    private String patronId;
    private String card;
    private String pin;
    private String email;
    private Signature expSignature;
    private String authorizationHeader;
    private AuthInfo actualAuthInfo;

    @Before
    public void setUpAuthInfoResolver() {
        underTest = new AuthInfoResolver();
        given(authInfoSecretKeyResolver.getSecretKey(anyLong())).willReturn(SECRET_KEY);
        given(authInfoSecretKeyResolver.isValidate()).willReturn(true);
        ReflectionTestUtils.setField(underTest, "authInfoSecretKeyResolver", authInfoSecretKeyResolver);
    }

    @Test
    public void validSignature_withPatronId() {
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
    public void validSignature_withPatronIdAndEmail() {
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

    private void givenAuthorizationHeader() {
        AuthInfo authInfo = new AuthInfo(EHUB_CONSUMER_ID, patron, SECRET_KEY);
        authorizationHeader = authInfo.toString();
    }

    private void givenValidSignature() {
        expSignature = new Signature(AuthInfo.getSignatureItems(EHUB_CONSUMER_ID, patron), SECRET_KEY);
    }

    private void givenPatron() {
        patron = new Patron.Builder(card, pin).id(patronId).email(email).build();
    }

    private void givenPatronId() {
        patronId = PATRON_ID;
    }

    private void whenResolve() {
        actualAuthInfo = underTest.resolve(authorizationHeader);
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

    @Test
    public void invalidSignature() {
        givenInvalidSignatureInHeader();
        try {
            whenResolve();
        } catch (UnauthorizedException e) {
            thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.INVALID_SIGNATURE);
        }
    }

    private void givenInvalidSignatureInHeader() {
        makeAuthorizationHeaderWithoutPatronIdButWithCardPin("invalidSignature");
    }

    private void makeAuthorizationHeaderWithoutPatronIdButWithCardPin(String signature) {
        authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_signature=\"{3}\"",
                EHUB_CONSUMER_ID, authInfoEncode(CARD), authInfoEncode(PIN), authInfoEncode(signature));
    }

    private void thenActualErrorCauseEqualsExpectedErrorCause(EhubRuntimeException e, ErrorCause expectedErrorCause) {
        ErrorCause actualErrorCause = getActualErrorCause(e);
        Assert.assertEquals(expectedErrorCause, actualErrorCause);
    }

    private ErrorCause getActualErrorCause(EhubRuntimeException e) {
        EhubError ehubError = e.getEhubError();
        return ehubError.getCause();
    }

    @Test
    public void validSignature_withoutPatronIdCardPin() {
        givenPatron();
        givenValidSignature();
        givenAuthorizationHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
    }
}
