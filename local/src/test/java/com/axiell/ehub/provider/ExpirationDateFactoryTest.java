package com.axiell.ehub.provider;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ExpirationDateFactoryTest {
    private static final int LOAN_EXPIRATION_DAYS = 30;
    private static final String INVALID_LOAN_EXPIRATION_DAYS = "invalid";

    private IExpirationDateFactory underTest;
    @Mock
    private ContentProvider contentProvider;
    private Date actualDate;

    @BeforeEach
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
        Assertions.assertTrue(actualDate.getTime() > new Date().getTime());
    }
}
