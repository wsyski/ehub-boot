package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OcdAuthenticatorTest {
    @Mock
    private ClientResponseFailure clientResponseFailure;
    @Mock
    private ClientResponse response;
    @Mock
    private IOcdFacade ocdFacade;
    @Mock
    private BearerToken bearerToken;
    @Mock
    private CommandData data;
    private OcdAuthenticator underTest;
    private BearerToken actualBearerToken;

    @Before
    public void setUpUnderTest() {
        underTest = new OcdAuthenticator();
        ReflectionTestUtils.setField(underTest, "ocdFacade", ocdFacade);
    }

    @Test
    public void authenticate_success() {
        givenBearerTokenFromOcdFacade();
        whenAuthenticate();
        thenActualBearerTokenEqualsExpectedBearerToken();
        thenNewBearerTokenInOcdFacadeIsInvokedOnce();
        thenAddPatronInOcdFacadeIsNeverInvoked();
    }

    private void givenBearerTokenFromOcdFacade() {
        given(ocdFacade.newBearerToken(any(ContentProviderConsumer.class), any(Patron.class))).willReturn(bearerToken);
    }

    private void whenAuthenticate() {
        actualBearerToken = underTest.authenticate(data);
    }

    private void thenActualBearerTokenEqualsExpectedBearerToken() {
        assertEquals(bearerToken, actualBearerToken);
    }

    private void thenNewBearerTokenInOcdFacadeIsInvokedOnce() {
        verify(ocdFacade).newBearerToken(any(ContentProviderConsumer.class), any(Patron.class));
    }

    private void thenAddPatronInOcdFacadeIsNeverInvoked() {
        verify(ocdFacade, never()).addPatron(any(ContentProviderConsumer.class), any(Patron.class));
    }

    @Test
    public void authenticate_unauthorized() {
        givenUnauthorizedStatusCode();
        givenClientResponseInClientResponseFailure();
        givenFirstClientResposneFailureThenBearerToken();
        whenAuthenticate();
        thenActualBearerTokenEqualsExpectedBearerToken();
        thenNewBearerTokenInOcdFacadeIsInvokedTwice();
        thenAddPatronInOcdFacadeIsInvokedOnce();
    }

    private void givenUnauthorizedStatusCode() {
        given(response.getStatus()).willReturn(401);
    }

    private void givenClientResponseInClientResponseFailure() {
        given(clientResponseFailure.getResponse()).willReturn(response);
    }

    private void givenFirstClientResposneFailureThenBearerToken() {
        given(ocdFacade.newBearerToken(any(ContentProviderConsumer.class), any(Patron.class))).willThrow(clientResponseFailure).willReturn(bearerToken);
    }

    private void thenNewBearerTokenInOcdFacadeIsInvokedTwice() {
        verify(ocdFacade, times(2)).newBearerToken(any(ContentProviderConsumer.class), any(Patron.class));
    }

    private void thenAddPatronInOcdFacadeIsInvokedOnce() {
        verify(ocdFacade).addPatron(any(ContentProviderConsumer.class), any(Patron.class));
    }

    @Test(expected = ClientResponseFailure.class)
    public void authenticate_internalError() {
        givenInternalErrorStatusCode();
        givenClientResponseInClientResponseFailure();
        givenFirstClientResponse();
        whenAuthenticate();
        thenNewBearerTokenInOcdFacadeIsInvokedOnce();
        thenAddPatronInOcdFacadeIsNeverInvoked();
    }

    private void givenInternalErrorStatusCode() {
        given(response.getStatus()).willReturn(500);
    }

    private void givenFirstClientResponse() {
        given(ocdFacade.newBearerToken(any(ContentProviderConsumer.class), any(Patron.class))).willThrow(clientResponseFailure);
    }
}
