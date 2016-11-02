package com.axiell.ehub.provider.ocd;

import org.junit.Ignore;
import org.junit.Test;

import static com.axiell.ehub.provider.ocd.PatronDTO.EMAIL_DOMAIN;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

/**
 * This test is ignored, when it can only be run once and then an exception is thrown since the patron already exists.
 * Remove the ignore annotation, change the card number and then run the test if adding a patron should be tested.
 */
public class OcdPatronIT extends AbstractOcdIT {
    private PatronDTO patronDTO;

    @Ignore
    @Test
    public void addPatron() {
        givenApiBaseUrl();
        givenLibraryId();
        givenBasicToken();
        givenContentProvider();
        whenAddPatron();
        thenPatronHasAnId();
    }

    @Test
    public void getPatron() {
        givenApiBaseUrl();
        givenLibraryId();
        givenBasicToken();
        givenContentProvider();
        whenGetPatronByEmail();
        thenPatronHasAnId();
    }

    private void whenAddPatron() {
        patronDTO = underTest.addPatron(contentProviderConsumer, patron);
    }

    private void thenPatronHasAnId() {
        assertNotNull(patronDTO.getPatronId());
    }

    private void whenGetPatronByEmail() {
        patronDTO = underTest.getPatron(contentProviderConsumer, patron);
    }
}
