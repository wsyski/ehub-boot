package com.axiell.ehub.provider.alias;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AliasTest {
    private Alias underTest;
    private String givenValue;
    private String actualValue;

    @Test
    public void lowerCase() {
        givenLowerCaseValue();
        givenSourceWithGivenValue();
        whenGetValue();
        thenActualValueEqualsUpperCaseOfGivenValue();
    }

    @Test
    public void camelCase() {
        givenCamelCaseValue();
        givenSourceWithGivenValue();
        whenGetValue();
        thenActualValueEqualsUpperCaseOfGivenValue();
    }

    @Test
    public void upperCase() {
        givenUpperCaseValue();
        givenSourceWithGivenValue();
        whenGetValue();
        thenActualValueEqualsUpperCaseOfGivenValue();
    }

    @Test
    public void setValue() {
        givenCamelCaseValue();
        givenSetValue();
        whenGetValue();
        thenActualValueEqualsUpperCaseOfGivenValue();
    }

    @Test
    public void testToString() {
        givenUpperCaseValue();
        givenSourceWithGivenValue();
        whenGetValue();
        thenActualValueEqualsToStringValueOfSource();
    }

    private void thenActualValueEqualsToStringValueOfSource() {
        Assertions.assertEquals(underTest.toString(), actualValue);
    }

    private void givenSetValue() {
        underTest = new Alias();
        underTest.setValue(givenValue);
    }

    private void givenUpperCaseValue() {
        givenValue = "GIVENVALUE";
    }

    private void givenCamelCaseValue() {
        givenValue = "givenValue";
    }

    private void givenLowerCaseValue() {
        givenValue = "givenvalue";
    }

    private void givenSourceWithGivenValue() {
        underTest = Alias.newInstance(givenValue);
    }

    private void whenGetValue() {
        actualValue = underTest.getValue();
    }

    private void thenActualValueEqualsUpperCaseOfGivenValue() {
        Assertions.assertEquals(givenValue.toUpperCase(), actualValue);
    }
}
