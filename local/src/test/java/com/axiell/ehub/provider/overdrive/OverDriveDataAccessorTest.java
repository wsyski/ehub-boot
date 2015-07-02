package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionFactoryStub;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO.DownloadLinkTemplateDTO;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links.ContentLink;
import com.axiell.ehub.provider.record.format.Format;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.EhubAssert.thenNotFoundExceptionIsThrown;
import static org.mockito.BDDMockito.given;

public class OverDriveDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "1";
    private static final String OVERDRIVE_FORMAT_NAME = "OverDriveFormat";

    @Rule
    public ExpectedException throwable = ExpectedException.none();

    @Mock
    private IOverDriveFacade overDriveFacade;
    @Mock
    private Product product;
    @Mock
    private DiscoveryFormatDTO discoveryFormat;
    @Mock
    private ErrorDTO errorDetails;
    @Mock
    private OAuthAccessToken accessToken;
    @Mock
    private CheckoutDTO checkout;
    @Mock
    private CirculationFormatDTO circulationFormat;
    @Mock
    private LinkTemplatesDTO linkTemplates;

    @Mock
    private DownloadLinkTemplateDTO downloadLinkTemplate;
    @Mock
    private DownloadLinkDTO downloadLink;
    @Mock
    private Links links;
    @Mock
    private ContentLink contentLink;
    @Mock
    private CheckoutsDTO checkouts;
    private IEhubExceptionFactory ehubExceptionFactory = new EhubExceptionFactoryStub();

    private OverDriveDataAccessor underTest;

    @Before
    public void setUpElibUDataAccessor() {
        underTest = new OverDriveDataAccessor();
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "overDriveFacade", overDriveFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void getFormatsForAvailableProduct() {
        givenFormatFromFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenProduct();
        givenProductAvailable(true);
        givenDiscoveryFormat();
        givenContentProvider();
        givenFormatIdInDiscoveryFormat();
        givenTextBundle();
        whenGetFormats();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void getFormatsForUnavailableProduct() {
        givenContentProviderName();
        givenFormatFromFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenProduct();
        givenProductAvailable(false);
        givenDiscoveryFormat();
        givenContentProvider();
        givenFormatIdInDiscoveryFormat();
        givenTextBundle();
        givenExpectedInternalServerException();
        whenGetFormats();
    }

    private void givenExpectedInternalServerException() {
        throwable.expect(InternalServerErrorException.class);
        throwable.expect(new ContentProviderErrorExceptionMatcher(InternalServerErrorException.class, ContentProviderName.OVERDRIVE,
                ErrorCauseArgumentValue.Type.PRODUCT_UNAVAILABLE.name()));
    }

    private void givenProduct() {
        given(overDriveFacade.getProduct(contentProviderConsumer, RECORD_ID)).willReturn(product);
    }

    private void givenDiscoveryFormat() {
        List<DiscoveryFormatDTO> discoveryFormats = Arrays.asList(discoveryFormat);
        given(product.getFormats()).willReturn(discoveryFormats);
    }

    private void givenProductAvailable(final boolean isAvailable) {
        given(product.isAvailable()).willReturn(isAvailable);
    }

    private void givenFormatIdInDiscoveryFormat() {
        given(discoveryFormat.getId()).willReturn(FORMAT_ID);
        given(discoveryFormat.getName()).willReturn(OVERDRIVE_FORMAT_NAME);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    private void thenFormatHasOverDriveName() {
        Assert.assertFalse(actualFormats.getFormats().isEmpty());
        Format actualFormat = actualFormats.getFormats().iterator().next();
        Assert.assertEquals(OVERDRIVE_FORMAT_NAME, actualFormat.name());
    }

    private void givenErrorDetails() {
        given(response.getEntity(ErrorDTO.class)).willReturn(errorDetails);
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenCheckout();
        givenExpirationDateInCheckout();
        givenCirculationFormats();
        givenFormatType();
        givenReserveId();
        givenLinkTemplates();
        givenDownloadLinkTemplate();
        givenDownloadLink();
        givenLinks();
        givenOvderDriveContentLink();
        givenDownloadUrl();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
        givenFormatIdFromFormatDecoration();
        givenDownloadableContentDisposition();
        givenContentLink();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    private void givenCirculationFormats() {
        List<CirculationFormatDTO> circulationFormats = Arrays.asList(circulationFormat);
        given(checkout.getFormats()).willReturn(circulationFormats);
    }

    private void givenFormatType() {
        given(circulationFormat.getFormatType()).willReturn(FORMAT_ID);
    }

    private void givenReserveId() {
        given(circulationFormat.getReserveId()).willReturn(RECORD_ID);
    }

    private void givenLinkTemplates() {
        given(circulationFormat.getLinkTemplates()).willReturn(linkTemplates);
    }

    private void givenDownloadLinkTemplate() {
        given(linkTemplates.getDownloadLink()).willReturn(downloadLinkTemplate);
    }

    public void givenPatronAccessToken() {
        given(overDriveFacade.getPatronOAuthAccessToken(contentProviderConsumer, CARD, PIN)).willReturn(accessToken);
    }

    private void givenCheckout() {
        given(overDriveFacade.checkout(contentProviderConsumer, accessToken, RECORD_ID, FORMAT_ID)).willReturn(checkout);
    }

    private void givenExpirationDateInCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
    }

    private void givenDownloadLink() {
        given(overDriveFacade.getDownloadLink(contentProviderConsumer, accessToken, downloadLinkTemplate)).willReturn(downloadLink);
    }

    private void givenLinks() {
        given(downloadLink.getLinks()).willReturn(links);
    }

    private void givenOvderDriveContentLink() {
        given(links.getContentLink()).willReturn(contentLink);
    }

    private void givenDownloadUrl() {
        given(contentLink.getHref()).willReturn(CONTENT_HREF);
    }

    private void givenFormatIdFromFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    @Test
    public void createLoanWhenNotSameFormat() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenCheckout();
        givenExpirationDateInCheckout();
        givenCirculationFormats();
        try {
            whenCreateLoan();
            Assert.fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    @Test
    public void getContent() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenCheckouts();
        givenRecordIdFromContentProviderLoanMetadata();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenCheckoutList();
        givenCirculationFormats();
        givenFormatType();
        givenReserveId();
        givenLinkTemplates();
        givenDownloadLinkTemplate();
        givenDownloadLink();
        givenLinks();
        givenOvderDriveContentLink();
        givenDownloadUrl();
        givenFormatIdFromFormatDecoration();
        givenDownloadableContentDisposition();
        givenContentLink();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    public void givenCheckouts() {
        given(overDriveFacade.getCheckouts(contentProviderConsumer, accessToken)).willReturn(checkouts);
    }

    public void givenRecordIdFromContentProviderLoanMetadata() {
        given(loanMetadata.getRecordId()).willReturn(RECORD_ID);
    }

    private void givenCheckoutList() {
        List<CheckoutDTO> checkoutList = Arrays.asList(checkout);
        given(checkouts.getCheckouts()).willReturn(checkoutList);
    }

    public void whenGetContent() {
        actualContentLink = underTest.getContent(commandData);
    }

    @Test
    public void getContentWhenNoDownloadLinkTemplate() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenLanguageInCommandData();
        givenPatronAccessToken();
        givenCheckouts();
        givenRecordIdFromContentProviderLoanMetadata();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenFormatIdFromFormatDecoration();
        givenClientResponse();
        givenClientResponseStatus();
        givenErrorDetails();
        try {
            whenGetContent();
            Assert.fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    private void givenContentProviderName() {
        given(contentProvider.getName()).willReturn(ContentProviderName.OVERDRIVE);
    }


}

