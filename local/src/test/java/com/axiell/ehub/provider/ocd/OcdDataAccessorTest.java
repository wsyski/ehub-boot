package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class OcdDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private OcdDataAccessor underTest;
    @Mock
    private IOcdFacade ocdFacade;
    @Mock
    private IFormatFactory formatFactory;
    @Mock
    private IOcdAuthenticator ocdAuthenticator;
    @Mock
    private BearerToken bearerToken;
    @Mock
    private IOcdCheckoutHandler ocdCheckoutHandler;
    @Mock
    private MediaDTO mediaDTO;
    @Mock
    private Format format;
    @Mock
    private Checkout checkout;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;

    @Before
    public void setUpUnderTest() {
        underTest = new OcdDataAccessor();
        ReflectionTestUtils.setField(underTest, "ocdFacade", ocdFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "ocdAuthenticator", ocdAuthenticator);
        ReflectionTestUtils.setField(underTest, "ocdCheckoutHandler", ocdCheckoutHandler);
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void getFormats_success() {
        givenContentProviderRecordIdInMedia();
        givenAllMedia();
        givenContentProviderRecordIdInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormat();
        whenGetFormats();
        thenFormatSetContainsOneFormat();
        thenFormatHasEhubFormatNameAndDescription();
    }

    private void givenContentProviderRecordIdInMedia() {
        given(mediaDTO.getTitleId()).willReturn(RECORD_ID);
    }

    private void givenAllMedia() {
        List<MediaDTO> allMedia = Lists.newArrayList(mediaDTO);
        given(ocdFacade.getAllMedia(contentProviderConsumer)).willReturn(allMedia);
    }

    private void givenFormat() {
        given(format.getName()).willReturn(EHUB_FORMAT_NAME);
        given(format.getDescription()).willReturn(EHUB_FORMAT_DESCRIPTION);
        given(formatFactory.create(any(ContentProvider.class), anyString(), anyString())).willReturn(format);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    @Test
    public void getFormats_MediaNotFound() {
        givenAllMedia();
        givenContentProviderRecordIdInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormat();
        whenGetFormats();
        thenFormatSetIsEmpty();
    }

    @Test
    public void createLoan_success() {
        givenBearerToken();
        givenContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProviderRecordIdInCommandData();
        givenCheckoutIsSuccessful();
        givenCheckout();
        givenCompleteCheckout();
        givenCreatedDownloadableContent();
        whenCreateLoan();
        thenActualLoanContainsDownloadUrl();
    }

    private void givenBearerToken() {
        given(ocdAuthenticator.authenticate(commandData)).willReturn(bearerToken);
    }

    public void givenCheckout() {
        given(ocdCheckoutHandler.checkout(any(BearerToken.class), any(CommandData.class))).willReturn(checkout);
    }

    public void givenCheckoutIsSuccessful() {
        given(checkout.isSuccessful()).willReturn(true);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getDownloadUrl()).willReturn(DOWNLOAD_URL);
        given(ocdCheckoutHandler.getCompleteCheckout(any(BearerToken.class), any(CommandData.class), anyString())).willReturn(checkout);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    @Test(expected = InternalServerErrorException.class)
    public void createLoan_unsuccessful() {
        givenBearerToken();
        givenInternalServerErrorException();
        givenCheckout();
        whenCreateLoan();
    }

    private void givenInternalServerErrorException() {
        given(ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(any(ContentProviderConsumer.class), any(ErrorCauseArgumentValue.Type.class), anyString())).willReturn(internalServerErrorException);
    }

    @Test
    public void getContent() {
        givenBearerToken();
        givenCompleteCheckout();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenContentProviderLoanMetadataInCommandData();
        givenCreatedDownloadableContent();
        whenGetContent();
        thenActualContentContainsDownloadUrl();
    }

    public void whenGetContent() {
        actualContent = underTest.getContent(commandData);
    }
}
