package com.axiell.ehub.provider.routing;


import junit.framework.Assert;
import org.junit.Test;

public class SourceTest {
    private Source underTest;
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
        Assert.assertEquals(actualValue, underTest.toString());
    }

    private void givenSetValue() {
        underTest = new Source();
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
        underTest = new Source(givenValue);
    }

    private void whenGetValue() {
        actualValue = underTest.getValue();
    }

    private void thenActualValueEqualsUpperCaseOfGivenValue() {
        Assert.assertEquals(givenValue.toUpperCase(), actualValue);
    }
}
