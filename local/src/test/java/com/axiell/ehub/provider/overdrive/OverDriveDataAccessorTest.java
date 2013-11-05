package com.axiell.ehub.provider.overdrive;

import static com.axiell.ehub.EhubAssert.thenNotFoundExceptionIsThrown;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links;
import com.axiell.ehub.provider.overdrive.DownloadLink.Links.ContentLink;
import com.axiell.ehub.provider.record.format.Format;

public class OverDriveDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "1";
    private static final String OVERDRIVE_FORMAT_NAME = "OverDriveFormat";

    @Mock
    private IOverDriveFacade overDriveFacade;
    @Mock
    private Product product;
    @Mock
    private DiscoveryFormat discoveryFormat;
    @Mock
    private ErrorDetails errorDetails;
    @Mock
    private OAuthAccessToken accessToken;
    @Mock
    private Checkout checkout;
    @Mock
    private CirculationFormat circulationFormat;
    @Mock
    private LinkTemplates linkTemplates;

    @Mock
    private DownloadLinkTemplate downloadLinkTemplate;
    @Mock
    private DownloadLink downloadLink;
    @Mock
    private Links links;
    @Mock
    private ContentLink contentLink;
    @Mock
    private Checkouts checkouts;
    private OverDriveDataAccessor underTest;

    @Before
    public void setUpElibUDataAccessor() {
	underTest = new OverDriveDataAccessor();
	ReflectionTestUtils.setField(underTest, "overDriveFacade", overDriveFacade);
    }

    @Test
    public void getFormatsWithOverDriveFormatNameWhenNoTextBundle() {
	givenProduct();
	givenDiscoveryFormat();
	givenContentProvider();
	givenFormatIdInDiscoveryFormat();
	givenFormatNameInDiscoveryFormat();
	whenGetFormats();
	thenFormatSetContainsOneFormat();
	thenFormatHasOverDriveName();
    }

    private void givenProduct() {
	given(overDriveFacade.getProduct(contentProviderConsumer, RECORD_ID)).willReturn(product);
    }

    private void givenDiscoveryFormat() {
	List<DiscoveryFormat> discoveryFormats = Arrays.asList(discoveryFormat);
	given(product.getFormats()).willReturn(discoveryFormats);
    }

    private void givenFormatIdInDiscoveryFormat() {
	given(discoveryFormat.getId()).willReturn(FORMAT_ID);
    }

    private void givenFormatNameInDiscoveryFormat() {
	given(discoveryFormat.getName()).willReturn(OVERDRIVE_FORMAT_NAME);
    }

    private void whenGetFormats() {
	actualFormats = underTest.getFormats(contentProviderConsumer, RECORD_ID, LANGUAGE);
    }

    private void thenFormatSetContainsOneFormat() {
	Set<Format> formatSet = thenFormatSetIsNotNull();
	Assert.assertTrue(formatSet.size() == 1);
    }

    private void thenFormatHasOverDriveName() {
	Assert.assertFalse(actualFormats.getFormats().isEmpty());
	Format actualFormat = actualFormats.getFormats().iterator().next();
	Assert.assertEquals(OVERDRIVE_FORMAT_NAME, actualFormat.getName());
    }

    private Set<Format> thenFormatSetIsNotNull() {
	Assert.assertNotNull(actualFormats);
	Set<Format> formatSet = actualFormats.getFormats();
	Assert.assertNotNull(formatSet);
	return formatSet;
    }

    @Test
    public void getFormatsWithEhubNameAndDescription() {
	givenProduct();
	givenDiscoveryFormat();
	givenContentProvider();
	givenFormatIdInDiscoveryFormat();
	givenTextBundle();
	givenEhubFormatNameAndDescription();
	whenGetFormats();
	thenFormatHasEhubFormatNameAndDescription();
    }

    @Test
    public void getFormatsWithOverDriveFormatNameWhenTextBundle() {
	givenProduct();
	givenDiscoveryFormat();
	givenContentProvider();
	givenFormatIdInDiscoveryFormat();
	givenFormatNameInDiscoveryFormat();
	givenTextBundle();
	whenGetFormats();
	thenFormatSetContainsOneFormat();
	thenFormatHasOverDriveName();
    }

    private void givenErrorDetails() {
	given(response.getEntity(ErrorDetails.class)).willReturn(errorDetails);
    }

    @Test
    public void createLoan() {
	givenPatronAccessToken();
	givenRecordIdInPendingLoan();
	givenFormatIdInPendingLoan();
	givenCheckout();
	givenExpirationDateInCheckout();
	givenCirculationFormats();
	givenFormatType();
	givenReserveId();
	givenLinkTemplates();
	givenDownloadLinkTemplate();
	givenDownloadLink();
	givenLinks();
	givenContentLink();
	givenDownloadUrl();
	givenContentProvider();
	givenFormatDecorationFromContentProvider();
	givenFormatIdFromFormatDecoration();
	givenContentDisposition();
	whenCreateLoan();
	thenActualLoanContainsDownloadUrl();
    }

    private void givenCirculationFormats() {
	List<CirculationFormat> circulationFormats = Arrays.asList(circulationFormat);
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

    private void givenContentLink() {
	given(links.getContentLink()).willReturn(contentLink);
    }

    private void givenDownloadUrl() {
	given(contentLink.getHref()).willReturn(DOWNLOAD_URL);
    }

    private void givenRecordIdInPendingLoan() {
	given(pendingLoan.getContentProviderRecordId()).willReturn(RECORD_ID);
    }

    private void givenFormatIdInPendingLoan() {
	given(pendingLoan.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    public void givenFormatIdFromFormatDecoration() {
	given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void whenCreateLoan() {
	actualLoan = underTest.createLoan(contentProviderConsumer, CARD, PIN, pendingLoan);
    }

    @Test
    public void createLoanWhenNotSameFormat() {
	givenPatronAccessToken();
	givenRecordIdInPendingLoan();
	givenFormatIdInPendingLoan();
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
	givenContentLink();
	givenDownloadUrl();
	givenFormatIdFromFormatDecoration();
	givenContentDisposition();
	whenGetContent();
	thenActualContentContainsDownloadUrl();
    }

    public void givenCheckouts() {
	given(overDriveFacade.getCheckouts(contentProviderConsumer, accessToken)).willReturn(checkouts);
    }

    public void givenRecordIdFromContentProviderLoanMetadata() {
	given(loanMetadata.getRecordId()).willReturn(RECORD_ID);
    }

    private void givenCheckoutList() {
	List<Checkout> checkoutList = Arrays.asList(checkout);
	given(checkouts.getCheckouts()).willReturn(checkoutList);
    }

    public void whenGetContent() {
	actualContent = underTest.getContent(contentProviderConsumer, CARD, PIN, loanMetadata);
    }
    
    @Test
    public void getContentWhenNoDownloadLinkTemplate() {
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
}
