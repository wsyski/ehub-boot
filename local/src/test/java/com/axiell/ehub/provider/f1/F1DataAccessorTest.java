package com.axiell.ehub.provider.f1;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.record.format.Format;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class F1DataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String F1_FORMAT_NAME = "F1Format";
    private static final String LOAN_ID = "loanId";
    private F1DataAccessor underTest;
    @Mock
    private IF1Facade f1Facade;
    @Mock
    private GetFormatResponse getFormatResponse;
    @Mock
    private Format format;
    @Mock
    private CreateLoanResponse createLoanResponse;
    @Mock
    private GetLoanContentResponse getLoanContentResponse;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private InternalServerErrorException internalServerErrorException;

    @Before
    public void setUpUnderTest() {
        underTest = new F1DataAccessor();
        ReflectionTestUtils.setField(underTest, "f1Facade", f1Facade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "expirationDateFactory", expirationDateFactory);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void getFormats() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProvider();
        givenFormatIdFromF1Facade();
        givenFormatFromFormatFactory();
        whenGetFormats();
        thenFormatSetContainsOneFormat();
        thenFormatHasF1Name();
    }

    @Test
    public void noFormats() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProvider();
        givenNoSuchFormatFromF1Facade();
        whenGetFormats();
        thenFormatSetIsEmpty();
    }

    private void givenNoSuchFormatFromF1Facade() {
        given(getFormatResponse.getValue()).willReturn(GetFormatResponse.NO_SUCH_FORMAT);
        givenGetFormatResponseFromF1Facade();
    }

    @Test
    public void createLoan_success() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenFormatIdFromFormatDecoration();
        givenFormatDecorationFromContentProvider();
        givenExpirationDate();
        givenContentProvider();
        givenDownloadableContentDisposition();
        givenLoanIdFromF1Facade();
        givenValidLoanContentFromGetLoanContentResponse();
        givenCreatedDownloadableContent();
        whenCreateLoan();
        thenActualLoanContainsDownloadUrl();
        thenActualLoanHasExpectedId();
        thenActualLoanHasExpirationDateCreatedByExpirationDateFactory();
    }

    @Test(expected = InternalServerErrorException.class)
    public void createLoan_noContentProviderLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenCreateLoanFailedFromF1Facade();
        givenInternalServerErrorException();
        whenCreateLoan();
    }

    @Test(expected = InternalServerErrorException.class)
    public void createLoan_missingContent() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenLoanIdFromF1Facade();
        givenGetLoanContentResponseFromF1Facade();
        givenInternalServerErrorException();
        whenCreateLoan();
    }

    @Test
    public void getContent_success() {
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenContentProviderLoanMetadataInCommandData();
        givenValidLoanContentFromGetLoanContentResponse();
        givenCreatedDownloadableContent();
        whenGetContent();
        thenActualContentContainsDownloadUrl();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getContent_missingContent() {
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenContentProviderLoanMetadataInCommandData();
        givenGetLoanContentResponseFromF1Facade();
        givenInternalServerErrorException();
        whenGetContent();
    }

    private void givenInternalServerErrorException() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(anyString(), any(ContentProviderConsumer.class), any(Type.class),         anyString())).willReturn(internalServerErrorException);
    }

    private void givenCreateLoanFailedFromF1Facade() {
        given(createLoanResponse.getValue()).willReturn("ERROR");
        givenCreateLoanResponseFromF1Facade();
    }

    private void whenGetContent() {
        actualContent = underTest.getContent(commandData);
    }

    private void givenLoanIdFromF1Facade() {
        given(createLoanResponse.isValidLoan()).willReturn(true);
        given(createLoanResponse.getValue()).willReturn(LOAN_ID);
        givenCreateLoanResponseFromF1Facade();
    }

    private void givenCreateLoanResponseFromF1Facade() {
        given(f1Facade.createLoan(any(CommandData.class))).willReturn(createLoanResponse);
    }

    private void givenValidLoanContentFromGetLoanContentResponse() {
        given(getLoanContentResponse.isValidContent()).willReturn(true);
        given(getLoanContentResponse.getValue()).willReturn(DOWNLOAD_URL);
        givenGetLoanContentResponseFromF1Facade();
    }

    private void givenGetLoanContentResponseFromF1Facade() {
        given(f1Facade.getLoanContent(any(CommandData.class))).willReturn(getLoanContentResponse);
    }

    private void thenActualLoanHasExpectedId() {
        assertEquals(LOAN_ID, actualLoan.getId());
    }

    private void givenFormatIdFromFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    private void givenFormatFromFormatFactory() {
        given(format.getName()).willReturn(F1_FORMAT_NAME);
        given(formatFactory.create(contentProvider, FORMAT_ID, LANGUAGE)).willReturn(format);
    }

    private void givenFormatIdFromF1Facade() {
        given(getFormatResponse.isValidFormat()).willReturn(true);
        given(getFormatResponse.getValue()).willReturn(FORMAT_ID);
        givenGetFormatResponseFromF1Facade();
    }

    private void givenGetFormatResponseFromF1Facade() {
        given(f1Facade.getFormats(any(CommandData.class))).willReturn(getFormatResponse);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    private void thenFormatHasF1Name() {
        Assert.assertFalse(actualFormats.getFormats().isEmpty());
        Format actualFormat = actualFormats.getFormats().iterator().next();
        assertEquals(F1_FORMAT_NAME, actualFormat.getName());
    }
}
