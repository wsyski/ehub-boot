package com.axiell.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class PatronTest {
    private static final String ID = "id";
    private static final String CARD = "12345";
    private static final String PIN = "pin";
    private String card;
    private String id;
    private Patron underTest;

    @Test
    public void providedPatronId() {
        givenPatronId();
        givenCard();
        whenNewPatron();
        thenActualIdEqualsProvidedId();
        thenAcatualCardEqualsExpectedCard();
        thenActualPinEqualsExpectedPin();
    }

    private void givenPatronId() {
        id = ID;
    }

    private void givenCard() {
        card = CARD;
    }

    private void whenNewPatron() {
        underTest = new Patron.Builder().id(id).libraryCard(card).pin(PIN).build();
    }

    private void thenActualIdEqualsProvidedId() {
        assertEquals(ID, underTest.getId());
    }

    private void thenAcatualCardEqualsExpectedCard() {
        assertEquals(CARD, underTest.getLibraryCard());
    }

    private void thenActualPinEqualsExpectedPin() {
        assertEquals(PIN, underTest.getPin());
    }

    @Test
    public void generatedPatronId() {
        givenCard();
        whenNewPatron();
        thenActualIdEqualsExpectedGeneratedId();
        thenAcatualCardEqualsExpectedCard();
        thenActualPinEqualsExpectedPin();
    }

    private void thenActualIdEqualsExpectedGeneratedId() {
        assertEquals("3627909a29c31381a071ec27f7c9ca97726182aed29a7ddd2e54353322cfb30abb9e3a6df2ac2c20fe23436311d678564d0c8d305930575f60e2d3d048184d79",
                underTest.getId());
    }

    @Test
    public void noCardOrId() {
        whenNewPatron();
        thenPatronHasNoId();
        thenPatronHasNoCard();
    }

    private void thenPatronHasNoId() {
        assertFalse(underTest.hasId());
    }

    private void thenPatronHasNoCard() {
        assertFalse(underTest.hasLibraryCard());
    }
}
