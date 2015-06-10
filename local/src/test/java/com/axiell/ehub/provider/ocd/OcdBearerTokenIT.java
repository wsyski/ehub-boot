package com.axiell.ehub.provider.ocd;

import org.junit.Test;

import javax.ws.rs.NotAuthorizedException;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

public class OcdBearerTokenIT extends AbstractOcdIT {
    private static final String CARD = "unknown";
    private static final String PIN = "pin";

    @Test(expected = NotAuthorizedException.class)
    public void newBearerToken_unknownCard() {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenUnknownCardPin();
        whenNewBearerToken();
        fail("A ClientResponseFailure should have been thrown");
    }

    private void whenNewBearerToken() {
        underTest.newBearerToken(contentProviderConsumer, patron);
    }

    private void givenUnknownCardPin() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
        given(patron.getPin()).willReturn(PIN);
    }
}
