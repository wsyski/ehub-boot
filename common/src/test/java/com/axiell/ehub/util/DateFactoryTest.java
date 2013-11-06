package com.axiell.ehub.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateFactoryTest {
    private Date providedDate;
    private Date actualDate;

    @Test
    public void createWhenDateIsNull() {
	whenCreate();
	thenActualDateIsNull();
    }

    private void whenCreate() {
	actualDate = DateFactory.create(providedDate);
    }

    private void thenActualDateIsNull() {
	Assert.assertNull(actualDate);
    }
    
    @Test
    public void create() {
	givenNonNullProvidedDate();
	whenCreate();
	thenActualDateTimeEqualsProvidedDateTime();
    }

    private void givenNonNullProvidedDate() {
	providedDate = new Date();
    }
    
    private void thenActualDateTimeEqualsProvidedDateTime() {
	Assert.assertEquals(providedDate.getTime(), actualDate.getTime());
    }
}
