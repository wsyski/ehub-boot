package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.ResponseAnalyser;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

public class OcdBearerTokenIT extends AbstractOcdIT {
    private static final String CARD = "unknown";
    private static final String PIN = "pin";

    @Test
    public void newBearerToken_unknownCard() {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenUnknownCardPin();
        try {
            whenNewBearerToken();
            fail("A ClientResponseFailure should have been thrown");
        } catch (ClientResponseFailure crf) {
            thenResponseIsUnauthorized(crf);
        }
    }

    private void thenResponseIsUnauthorized(ClientResponseFailure crf) {
        ResponseAnalyser responseAnalyser = ResponseAnalyser.from(crf);
        assertTrue(responseAnalyser.isUnauthorized());
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
