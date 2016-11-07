package com.axiell.ehub.provider.ocd;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class OcdPatronIT extends AbstractOcdIT {
    private String patronId;

    @Test
    public void getOrCreatePatron() {
        givenApiBaseUrl();
        givenLibraryId();
        givenBasicToken();
        givenContentProvider();
        whenGetOrCreatePatron();
        thenPatronHasAnId();
    }

    private void whenGetOrCreatePatron() {
        patronId = underTest.getOrCreatePatron(contentProviderConsumer, patron);
    }

    private void thenPatronHasAnId() {
        assertNotNull(patronId);
    }
}
