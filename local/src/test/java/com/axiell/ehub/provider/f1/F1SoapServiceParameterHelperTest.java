package com.axiell.ehub.provider.f1;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Locale;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.F1_REGION_ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class F1SoapServiceParameterHelperTest {
    private static final String VALID_ID = "123";
    private static final String INVALID_ID = "ABC123";
    private static final String LANG = Locale.ENGLISH.getLanguage();
    private F1SoapServiceParameterHelper underTest;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private BadRequestException badRequestException;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    private String contentProviderRecordId;
    private String formatId;
    private String loanId;
    private Integer actualId;


    @Before
    public void setUpUnderTest() {
        underTest = new F1SoapServiceParameterHelper();
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void getMediaId_validId() {
        givenValidRecordId();
        whenGetMediaId();
        thenActualIdEqualsExpectedId();
    }

    @Test(expected = BadRequestException.class)
    public void getMediaId_invalidId() {
        givenInvalidRecordId();
        givenBadRequestExceptionIsCreated();
        whenGetMediaId();
    }

    @Test
    public void getRegionId_validId() {
        givenValidRegionId();
        whenGetRegionId();
        thenActualIdEqualsExpectedId();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getRegionId_invalidId() {
        givenInvalidRegionId();
        givenInternalServerErrorExceptionIsCreated();
        whenGetRegionId();
    }

    @Test
    public void getTypeId_validId() {
        givenValidTypeId();
        whenGetTypeId();
        thenActualIdEqualsExpectedId();
    }

    @Test(expected = BadRequestException.class)
    public void getTypeId_invalidId() {
        givenInvalidTypeId();
        givenBadRequestExceptionIsCreated();
        whenGetTypeId();
    }

    @Test
    public void getLoanId_validId() {
        givenValidLoanId();
        whenGetLoanId();
        thenActualIdEqualsExpectedId();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getLoanId_invalidId() {
        givenInvalidLoanId();
        givenInternalServerErrorExceptionIsCreated();
        whenGetRegionId();
    }

    private void givenValidLoanId() {
        loanId = VALID_ID;
    }

    private void whenGetLoanId() {
        actualId = underTest.getLoanId(contentProviderConsumer, loanId, LANG);
    }

    private void givenInvalidLoanId() {
        loanId = INVALID_ID;
    }

    private void givenValidTypeId() {
        formatId = VALID_ID;
    }

    private void givenInvalidTypeId() {
        formatId = INVALID_ID;
    }

    private void givenInvalidRegionId() {
        given(contentProviderConsumer.getProperty(F1_REGION_ID)).willReturn(INVALID_ID);
    }

    private void givenValidRegionId() {
        given(contentProviderConsumer.getProperty(F1_REGION_ID)).willReturn(VALID_ID);
    }

    private void whenGetRegionId() {
        actualId = underTest.getRegionId(contentProviderConsumer, LANG);
    }

    private void givenInvalidRecordId() {
        contentProviderRecordId = INVALID_ID;
    }

    private void givenBadRequestExceptionIsCreated() {
        given(ehubExceptionFactory.createBadRequestExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class), any(ErrorCauseArgumentType.class), any(String.class))).willReturn(badRequestException);
    }

    private void givenInternalServerErrorExceptionIsCreated() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class), any(ErrorCauseArgumentType.class), any(String.class))).willReturn(internalServerErrorException);
    }

    private void givenValidRecordId() {
        contentProviderRecordId = VALID_ID;
    }

    private void thenActualIdEqualsExpectedId() {
        Assert.assertEquals(Integer.valueOf(VALID_ID), actualId);
    }

    private void whenGetMediaId() {
        actualId = underTest.getMediaId(contentProviderConsumer, contentProviderRecordId, LANG);
    }

    private void whenGetTypeId() {
        actualId = underTest.getTypeId(contentProviderConsumer, formatId, LANG);
    }
}
