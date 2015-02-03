package com.axiell.ehub.provider.ocd;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

/**
 * This test is ignored, when it can only be run once and then an exception is thrown since the patron already exists.
 * Remove the ignore annotation, change the card number and then run the test if adding a patron should be tested.
 */
@Ignore
public class OcdPatronIT extends AbstractOcdIT {
    private static final String CARD = "123456789";
    private static final String PIN = "1111";
    private PatronDTO patronDTO;

    @Test
    public void addPatron() {
        givenApiBaseUrl();
        givenLibraryId();
        givenBasicToken();
        givenContentProvider();
        givenCardPin();
        whenAddPatron();
        thenPatronHasAnId();
    }

    private void givenCardPin() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
        given(patron.getPin()).willReturn(PIN);
    }

    private void whenAddPatron() {
        patronDTO = underTest.addPatron(contentProviderConsumer, patron);
    }

    private void thenPatronHasAnId() {
        assertNotNull(patronDTO.getPatronId());
    }
}
