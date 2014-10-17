package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.patron.Patron;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.MessageFormat;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoResolverTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private static final String SECRET_KEY = "secret";
    private static final String PATRON_ID = "patronId";
    private static final String CARD = "libraryCard";
    private static final String PIN = "pin";

    private AuthInfoResolver underTest;
    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private IPalmaDataAccessor palmaDataAccessor;
    @Mock
    private EhubConsumer ehubConsumer;
    private Patron patron;
    private String patronId;
    private String authorizationHeader;
    private AuthInfo actualAuthInfo;

    @Before
    public void setUpAuthInfoResolver() {
        underTest = new AuthInfoResolver();
        given(ehubConsumer.getSecretKey()).willReturn(SECRET_KEY);
        given(consumerBusinessController.getEhubConsumer(anyLong())).willReturn(ehubConsumer);
        ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
        ReflectionTestUtils.setField(underTest, "palmaDataAccessor", palmaDataAccessor);
    }

    @Test
    public void validSignature_withPatronId() {
        givenPatronId();
        givenPatron();
        givenValidSignatureInHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
    }

    private void givenValidSignatureInHeader() {
        final Signature expSignature = new Signature(EHUB_CONSUMER_ID, SECRET_KEY, patronId, CARD, PIN);
        AuthInfo authInfo = new AuthInfo(EHUB_CONSUMER_ID, patron, expSignature);
        authorizationHeader = authInfo.toString();
    }

    private void givenPatron() {
        patron = new Patron.Builder(CARD, PIN).id(patronId).build();
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
        givenPatron();
        givenValidSignatureInHeader();
        whenResolve();
        thenAuthInfoIsNotNull();
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
        makeAuthorizationHeader("invalidSignature");
    }

    private void makeAuthorizationHeader(String signature) {
        authorizationHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                EHUB_CONSUMER_ID, PATRON_ID, CARD, PIN, signature);
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
