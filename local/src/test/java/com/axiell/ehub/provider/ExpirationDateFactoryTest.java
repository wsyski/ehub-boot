package com.axiell.ehub.provider;

import static org.mockito.BDDMockito.given;

import java.util.Date;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

@RunWith(MockitoJUnitRunner.class)
public class ExpirationDateFactoryTest {
    private static final int LOAN_EXPIRATION_DAYS = 30;
    private static final String INVALID_LOAN_EXPIRATION_DAYS = "invalid";

    private IExpirationDateFactory underTest;
    @Mock
    private ContentProvider contentProvider;    
    private Date actualDate;

    @Before
    public void setUpExpirationDateFactory() {
	underTest = new ExpirationDateFactory();
    }

    @Test
    public void createExpirationDateWhenPropertyNotSet() {
	try {
	    whenCreateLoanExpirationDays();
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void whenCreateLoanExpirationDays() {
	actualDate = underTest.createExpirationDate(contentProvider);
    }

    @Test
    public void createExpirationDateWhenInvalidProperty() {
	givenInvalidLoanExpirationDays();
	try {
	    whenCreateLoanExpirationDays();
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void givenInvalidLoanExpirationDays() {
	given(contentProvider.getProperty(ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS)).willReturn(INVALID_LOAN_EXPIRATION_DAYS);
    }

    @Test
    public void createExpirationDate() {
	givenLoanExpirationDays();
	whenCreateLoanExpirationDays();
	thenActualDateEqualsExpectedDate();
    }
    
    private void givenLoanExpirationDays() {
	given(contentProvider.getProperty(ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS)).willReturn(String.valueOf(LOAN_EXPIRATION_DAYS));
    }

    private void thenActualDateEqualsExpectedDate() {
	DateTime today = DateTime.now(DateTimeZone.UTC).withHourOfDay(12);
	DateTime actualExpirationDateAsDateTime = new DateTime(DateTimeZone.UTC).withMillis(actualDate.getTime());
	Days actualDaysBetween = Days.daysBetween(today, actualExpirationDateAsDateTime);
	assertActualDaysBetweenEquals30Or29(actualDaysBetween);
    }
    
    private void assertActualDaysBetweenEquals30Or29(Days actualDaysBetween) {
	int days = actualDaysBetween.getDays();
	Assert.assertTrue(days == LOAN_EXPIRATION_DAYS || days == LOAN_EXPIRATION_DAYS - 1);
    }
}
