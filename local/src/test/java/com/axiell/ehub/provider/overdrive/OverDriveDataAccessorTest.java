package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionFactoryStub;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links;
import com.axiell.ehub.provider.overdrive.DownloadLinkDTO.Links.ContentLink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.axiell.ehub.EhubAssert.thenNotFoundExceptionIsThrown;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OverDriveDataAccessorTest extends ContentProviderDataAccessorTestFixture<OverDriveDataAccessor> {
    private static final String OVERDRIVE_FORMAT_NAME = "OverDriveFormat";
    protected static final String OVERDRIVE_RECORD_ID = "overdriveRecordId";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    private Map<String, DownloadLinkTemplateDTO> CIRCULATION_LINKS = Collections.singletonMap("downloadRedirect", new DownloadLinkTemplateDTO(CONTENT_HREF));
    @Mock
    private LinkTemplatesDTO linkTemplates;

    private DownloadLinkTemplateDTO DOWNLOAD_LINK_TEMPLATE = new DownloadLinkTemplateDTO(CONTENT_HREF);
    private DownloadLinkDTO DOWNLOAD_LINK = new DownloadLinkDTO(CONTENT_HREF);
    @Mock
    private Links links;
    @Mock
    private ContentLink contentLink;
    @Mock
    private CheckoutsDTO checkouts;
    private IEhubExceptionFactory ehubExceptionFactory = new EhubExceptionFactoryStub();

    @Before
    public void setUpElibUDataAccessor() {
        underTest = new OverDriveDataAccessor();
        ReflectionTestUtils.setField(underTest, "overDriveFacade", overDriveFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void getFormatsForAvailableProduct() {
        givenFormatInFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenProduct();
        givenProductAvailable(true);
        givenDiscoveryFormat();
        givenFormatIdInDiscoveryFormat();
        givenTextBundle();
        whenGetIssues();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void getFormatsForBorrowedProduct() {
        givenFormatInFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenCheckouts();
        givenCheckoutList();
        givenGetCirculationFormats();
        givenCirculationLinks();
        givenCirculationFormatType();
        givenTextBundle();
        givenProduct();
        whenGetIssues();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void getFormatsForUnavailableProduct() {
        givenFormatInFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLanguageInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenProduct();
        givenProductAvailable(false);
        givenDiscoveryFormat();
        givenFormatIdInDiscoveryFormat();
        givenTextBundle();
        givenExpectedContentProviderErrorException(ErrorCauseArgumentType.PRODUCT_UNAVAILABLE.name());
        whenGetIssues();
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
        givenCirculationLinks();
        givenCirculationFormatType();
        givenCirculationFormatReserveId();
        givenCirculationFormatLinkTemplates();
        givenDownloadLinkTemplate();
        givenDownloadLink();
        givenOverDriveContentLink();
        givenDownloadUrl();
        givenFormatDecorationInContentProvider();
        givenFormatIdFromFormatDecoration();
        givenDownloadableContentDisposition();
        givenProduct();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void createLoanWhenWithNewFormat() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenCheckouts();
        givenCheckoutList();
        givenGetCirculationFormats();
        givenCirculationLinks();
        givenCirculationFormatType();
        givenExpirationDateInCheckout();
        givenCirculationFormatLinkTemplates();
        givenDownloadLinkTemplate();
        givenLockFormat();
        givenDownloadUrl();
        givenDownloadLink();
        givenFormatIdFromFormatDecoration();
        givenDownloadableContentDisposition();
        givenProduct();
        givenOverDriveContentLink();
        givenFormatDecorationInContentProvider();
        givenFormatIdFromFormatDecoration();
        givenDownloadableContentDisposition();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
        thenLockFormatIsExecuted();
    }

    @Test
    public void getContent() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenPatronAccessToken();
        givenCheckouts();
        givenRecordIdFromContentProviderLoanMetadata();
        givenFormatDecorationInContentProviderLoanMetadata();
        givenCheckoutList();
        givenCirculationLinks();
        givenCirculationFormatType();
        givenCirculationFormatReserveId();
        givenCirculationFormatLinkTemplates();
        givenDownloadLinkTemplate();
        givenDownloadLink();
        givenOverDriveContentLink();
        givenDownloadUrl();
        givenFormatIdFromFormatDecoration();
        givenDownloadableContentDisposition();
        givenProduct();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    @Test
    public void getContentWhenNoDownloadLinkTemplate() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenPinInPatron();
        givenLanguageInCommandData();
        givenPatronAccessToken();
        givenCheckouts();
        givenRecordIdFromContentProviderLoanMetadata();
        givenFormatDecorationInContentProviderLoanMetadata();
        givenFormatIdFromFormatDecoration();
        givenClientResponse();
        givenClientResponseStatus();
        givenProduct();
        givenErrorDetails();
        try {
            whenGetContent();
            Assert.fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    private void givenLockFormat() {
        given(overDriveFacade.lockFormat(contentProviderConsumer, accessToken, OVERDRIVE_RECORD_ID, FORMAT_ID)).willReturn(circulationFormat);
    }

    private void thenLockFormatIsExecuted() {
        verify(overDriveFacade, times(1)).lockFormat(contentProviderConsumer, accessToken, OVERDRIVE_RECORD_ID, FORMAT_ID);
    }

    private void givenExpectedContentProviderErrorException(final String status) {
        expectedException.expect(new ContentProviderErrorExceptionMatcher(InternalServerErrorException.class, ContentProvider.CONTENT_PROVIDER_OVERDRIVE, status));
    }

    private void givenProduct() {
        given(overDriveFacade.getProduct(contentProviderConsumer, RECORD_ID, FORMAT_ID)).willReturn(product);
        given(product.getCrossRefId()).willReturn(RECORD_ID);
        given(product.getId()).willReturn(OVERDRIVE_RECORD_ID);
    }

    private void givenDiscoveryFormat() {
        List<DiscoveryFormatDTO> discoveryFormats = Collections.singletonList(discoveryFormat);
        given(product.getFormats()).willReturn(discoveryFormats);
    }

    private void givenProductAvailable(final boolean isAvailable) {
        given(product.isAvailable()).willReturn(isAvailable);
    }

    private void givenFormatIdInDiscoveryFormat() {
        given(discoveryFormat.getId()).willReturn(FORMAT_ID);
        given(discoveryFormat.getName()).willReturn(OVERDRIVE_FORMAT_NAME);
    }

    private void givenErrorDetails() {
        given(response.readEntity(ErrorDTO.class)).willReturn(errorDetails);
    }

    private void givenCirculationLinks() {
        given(checkout.getLinks()).willReturn(CIRCULATION_LINKS);
        given(checkout.getReserveId()).willReturn(OVERDRIVE_RECORD_ID);
    }

    private void givenCirculationFormatType() {
        given(circulationFormat.getFormatType()).willReturn(FORMAT_ID);
    }

    private void givenCirculationFormatReserveId() {
        given(circulationFormat.getReserveId()).willReturn(OVERDRIVE_RECORD_ID);
    }

    private void givenCirculationFormatLinkTemplates() {
        given(circulationFormat.getLinkTemplates()).willReturn(linkTemplates);
    }

    private void givenDownloadLinkTemplate() {
        given(linkTemplates.getDownloadLink()).willReturn(DOWNLOAD_LINK_TEMPLATE);
    }

    public void givenPatronAccessToken() {
        given(overDriveFacade.getPatronOAuthAccessToken(contentProviderConsumer, CARD, PIN)).willReturn(accessToken);
    }

    private void givenCheckout() {
        given(overDriveFacade.checkout(contentProviderConsumer, accessToken, OVERDRIVE_RECORD_ID, FORMAT_ID)).willReturn(checkout);
    }

    private void givenGetCirculationFormats() {
        CirculationFormatsDTO circulationFormats = new CirculationFormatsDTO();
        ReflectionTestUtils.setField(circulationFormats, "formats", Collections.singletonList(circulationFormat));
        given(overDriveFacade.getCirculationFormats(contentProviderConsumer, accessToken, OVERDRIVE_RECORD_ID)).willReturn(circulationFormats);
    }

    private void givenExpirationDateInCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
    }

    private void givenDownloadLink() {
        given(overDriveFacade.getDownloadLink(Mockito.eq(contentProviderConsumer), Mockito.eq(accessToken), Mockito.any(DOWNLOAD_LINK_TEMPLATE.getClass()))).willReturn(DOWNLOAD_LINK);
    }

    private void givenOverDriveContentLink() {
        given(links.getContentLink()).willReturn(contentLink);
    }

    private void givenDownloadUrl() {
        given(contentLink.getHref()).willReturn(CONTENT_HREF);
    }

    private void givenFormatIdFromFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void givenCheckouts() {
        given(overDriveFacade.getCheckouts(contentProviderConsumer, accessToken)).willReturn(checkouts);
    }

    private void givenRecordIdFromContentProviderLoanMetadata() {
        given(loanMetadata.getRecordId()).willReturn(RECORD_ID);
    }

    private void givenCheckoutList() {
        List<CheckoutDTO> checkoutList = Collections.singletonList(checkout);
        given(checkouts.getCheckouts()).willReturn(checkoutList);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_OVERDRIVE;
    }
}

