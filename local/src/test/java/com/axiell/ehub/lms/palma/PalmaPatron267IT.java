package com.axiell.ehub.lms.palma;

import com.axiell.ehub.patron.Patron;
import org.junit.Test;

import javax.xml.ws.Endpoint;

import static junit.framework.Assert.assertEquals;

public class PalmaPatron267IT extends AbstractPalmaIT<PalmaPatronService_267> {
    private String patronId;
    private String card;
    private String pin;
    private Patron actualPatron;

    @Override
    Endpoint makeEndpoint(String palmaUrl) {
        PalmaPatronService_267 palmaPatronService = new PalmaPatronService_267();
        return Endpoint.publish(palmaUrl + "/v267/patron", palmaPatronService);
    }

    @Test
    public void authenticatePatron() {
        givenNoPatronId();
        givenCard();
        givenPin();
        whenAuthenticatePatron();
        thenActualPatronIdEqualsExpectedPatronId();
        thenActualCardEqualsExpectedCard();
        thenActualPinEqualsExpectedPin();
    }

    private void givenNoPatronId() {
        patronId = null;
    }

    private void givenCard() {
        card = "12345";
    }

    private void givenPin() {
        pin = "1111";
    }

    private void whenAuthenticatePatron() {
        actualPatron = palmaDataAccessor.authenticatePatron(ehubConsumer, patronId, card, pin);
    }

    private void thenActualPatronIdEqualsExpectedPatronId() {
        assertEquals("8Lp+nsRJNphwg5hOdXdyvACD3X0nwSeei+EzpaZdEO4=", actualPatron.getId());
    }

    private void thenActualCardEqualsExpectedCard() {
        assertEquals(card, actualPatron.getLibraryCard());
    }

    private void thenActualPinEqualsExpectedPin() {
        assertEquals(pin, actualPatron.getPin());
    }
}
