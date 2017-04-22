package com.axiell.ehub.security;

import com.axiell.ehub.patron.Patron;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SignatureTest {
    private static final String SIGNATURE = "FXJVD5e14lkgq8lmY751Rf5XF+I=";
    private static final String COMPATIBILITY_SIGNATURE = "GN+9mlD70ZER/x3ur7w7HJRgnYU=";
    private Long ehubConsumerId;
    private String secret;
    private String patronId;
    private String card;
    private String pin;
    private Signature underTest;
    private String actValue;

    @Test
    public void toString_noPatronId() {
        givenEhubConsumerId();
        givenSecret();
        givenCard();
        givenPin();
        givenNewCompatibilitySignature();
        whenToString();
        thenActualSignatureEqualsExpectedCompatibilitySignature();
    }

    private void thenActualSignatureEqualsExpectedSignature() {
        assertEquals(SIGNATURE, actValue);
    }

    private void thenActualSignatureEqualsExpectedCompatibilitySignature() {
        assertEquals(COMPATIBILITY_SIGNATURE, actValue);
    }

    private void whenToString() {
        actValue = underTest.toString();
    }

    private void givenEhubConsumerId() {
        ehubConsumerId = 1L;
    }

    private void givenSecret() {
        secret = "secret1";
    }

    private void givenCard() {
        card = "909265910";
    }

    private void givenPin() {
        pin = "4447";
    }

    private void givenNewCompatibilitySignature() {
        Patron patron = new Patron.Builder(card, pin).id(patronId).build();
        underTest = new Signature(EhubAuthHeaderParser.getSignatureCompatibilityItems(ehubConsumerId, patron), secret);
    }

    private void givenNewSignature() {
        Patron patron = new Patron.Builder(card, pin).id(patronId).build();
        underTest = new Signature(EhubAuthHeaderParser.getSignatureItems(ehubConsumerId, patron), secret);
    }

    @Test
    public void toString_withPatronId_compatibility() {
        givenEhubConsumerId();
        givenSecret();
        givenPatronId();
        givenCard();
        givenPin();
        givenNewCompatibilitySignature();
        whenToString();
        thenActualSignatureEqualsExpectedCompatibilitySignature();
    }

    @Test
    public void toString_withPatronId() {
        givenEhubConsumerId();
        givenSecret();
        givenPatronId();
        givenCard();
        givenPin();
        givenNewSignature();
        whenToString();
        thenActualSignatureEqualsExpectedSignature();
    }

    private void givenPatronId() {
        patronId = "abc123";
    }
}
