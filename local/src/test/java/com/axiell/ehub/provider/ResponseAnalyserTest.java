package com.axiell.ehub.provider;

import junit.framework.Assert;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ResponseAnalyserTest {
    private ResponseAnalyser underTest;
    @Mock
    private ClientResponseFailure clientResponseFailure;
    @Mock
    private ClientResponse response;
    private boolean unauthorized;

    @Test
    public void isUnauthorized() {
        givenUnauthorizedStatusCode();
        givenClientResponseInClientResponseFailure();
        givenResponseAnaylserFromClientResponseFailure();
        whenIsUnAuthorized();
        thenIsUnauthorized();
    }

    private void thenIsUnauthorized() {
        assertTrue(unauthorized);
    }

    private void whenIsUnAuthorized() {
        unauthorized = underTest.isUnauthorized();
    }

    private void givenUnauthorizedStatusCode() {
        given(response.getStatus()).willReturn(401);
    }

    private void givenClientResponseInClientResponseFailure() {
        given(clientResponseFailure.getResponse()).willReturn(response);
    }

    private void givenResponseAnaylserFromClientResponseFailure() {
        underTest = ResponseAnalyser.from(clientResponseFailure);
    }
}
