package com.axiell.ehub.common.util;

import com.axiell.ehub.common.util.DateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

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
        Assertions.assertNull(actualDate);
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
        Assertions.assertEquals(providedDate.getTime(), actualDate.getTime());
    }
}
