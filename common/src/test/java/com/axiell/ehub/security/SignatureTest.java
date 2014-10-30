/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static org.junit.Assert.*;

import com.axiell.ehub.patron.Patron;
import org.junit.Test;

public class SignatureTest {
    private static final String EXP_VALUE = "GN%2B9mlD70ZER%2Fx3ur7w7HJRgnYU%3D";
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
        givenNewSignature();
        whenToString();
        thenActualSigntaureEqualsExpectedSignature();
    }

    private void thenActualSigntaureEqualsExpectedSignature() {
        assertEquals(EXP_VALUE, actValue);
    }

    private void whenToString() {
        actValue = underTest.toString();
    }

    private void givenEhubConsumerId() {
        ehubConsumerId = 1L;
    }

    private void givenSecret() {
        secret ="secret1";
    }

    private void givenCard() {
        card = "909265910";
    }

    private void givenPin() {
        pin ="4447";
    }

    private void givenNewSignature() {
        Patron patron = new Patron.Builder(card, pin).id(patronId).build();
        underTest = new Signature(ehubConsumerId, secret, patron);
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
        thenActualSigntaureEqualsExpectedSignature();
    }

    private void givenPatronId() {
        patronId = "abc123";
    }
}
