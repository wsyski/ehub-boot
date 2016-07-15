package com.axiell.ehub.provider.elib.elibu;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.elib.elibu.ConsumedProduct.Content;
import com.axiell.ehub.provider.elib.elibu.Product.AvailableFormat;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static com.axiell.ehub.EhubAssert.thenInternalServerErrorExceptionIsThrown;
import static com.axiell.ehub.EhubAssert.thenNotFoundExceptionIsThrown;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class ElibUDataAccessorTest extends ContentProviderDataAccessorTestFixture<ElibUDataAccessor> {
    private static final Integer LICENSE_ID = 1;

    @Mock
    private IElibUFacade elibUFacade;
    @Mock
    private Response response;
    @Mock
    private Result result;
    @Mock
    private Status status;
    @Mock
    private Product product;
    @Mock
    private AvailableFormat availableFormat;
    @Mock
    private FormatTextBundle textBundle;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private License license;
    @Mock
    private ConsumedProduct consumedProduct;
    @Mock
    private ConsumedProduct.Format consumedProductFormat;
    @Mock
    private Content content;

    @Before
    public void setUpElibUDataAccessor() {
        underTest = new ElibUDataAccessor();
        ReflectionTestUtils.setField(underTest, "elibUFacade", elibUFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
    }

    @Test
    public void getFormats() {
        givenFormatInFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenProductResponse();
        givenResult();
        givenStatus();
        givenStatusHasRetrievedProduct();
        givenProduct();
        givenAvailableFormats();
        givenFormatIdInAvailableFormat();
        givenTextBundle();
        whenGetIssues();
        thenActualFormatsContainsOneFormat();
    }

    private void givenProductResponse() {
        given(elibUFacade.getProduct(contentProviderConsumer, RECORD_ID)).willReturn(response);
    }

    private void givenResult() {
        given(response.getResult()).willReturn(result);
    }

    private void givenStatus() {
        given(result.getStatus()).willReturn(status);
    }

    private void givenStatusHasRetrievedProduct() {
        given(status.hasRetrievedProduct()).willReturn(true);
    }

    private void givenProduct() {
        given(result.getProduct()).willReturn(product);
    }

    private void givenAvailableFormats() {
        List<AvailableFormat> availableFormats = Collections.singletonList(availableFormat);
        given(product.getFormats()).willReturn(availableFormats);
    }

    private void givenFormatIdInAvailableFormat() {
        given(availableFormat.getId()).willReturn(FORMAT_ID);
    }

    @Test
    public void getFormatsWhenNoFormatId() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenProductResponse();
        givenResult();
        givenStatus();
        givenStatusHasRetrievedProduct();
        givenProduct();
        givenAvailableFormats();
        whenGetIssues();
        thenFormatsEmpty();
    }

    @Test
    public void getFormatsWhenProductHasNotBeenRetrieved() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenProductResponse();
        givenResult();
        givenStatus();
        givenStatusHasNotRetrievedProduct();
        try {
            whenGetIssues();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenStatusHasNotRetrievedProduct() {
        given(status.hasRetrievedProduct()).willReturn(false);
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenFormatDecorationInContentProvider();
        givenConsumeProductResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedProduct();
        givenConsumedProduct();
        givenConsumedProductFormats();
        givenFormatIdFromFormatDecoration();
        givenIsSameFormat();
        givenContentUrl();
        givenDownloadableContentDisposition();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    private void givenConsumeLicenseResponse() {
        given(elibUFacade.consumeLicense(contentProviderConsumer, CARD)).willReturn(response);
    }

    private void givenStatusIsConsumedLicense() {
        given(status.isConsumedLicense()).willReturn(true);
    }

    private void givenLicenseId() {
        given(result.getLicense()).willReturn(license);
        given(license.getLicenseId()).willReturn(LICENSE_ID);
    }

    private void givenConsumeProductResponse() {
        given(elibUFacade.consumeProduct(contentProviderConsumer, LICENSE_ID, RECORD_ID)).willReturn(response);
    }

    private void givenStatusIsConsumedProduct() {
        given(status.isConsumedProduct()).willReturn(true);
    }

    private void givenConsumedProduct() {
        given(result.getConsumedProduct()).willReturn(consumedProduct);
    }

    private void givenConsumedProductFormats() {
        List<ConsumedProduct.Format> formats = Collections.singletonList(consumedProductFormat);
        given(consumedProduct.getFormats()).willReturn(formats);
    }

    private void givenFormatIdFromFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void givenIsSameFormat() {
        given(consumedProductFormat.isSameFormat(any(String.class))).willReturn(true);
    }

    private void givenContentUrl() {
        given(consumedProductFormat.getContent()).willReturn(content);
        given(content.getUrl()).willReturn(ContentLinkBuilder.HREF);
    }

    @Test
    public void createLoanWhenMissingFormat() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        try {
            whenCreateLoan();
        } catch (BadRequestException e) {
            thenBadRequestExceptionIsThrown(e);
        }
    }

    private void thenBadRequestExceptionIsThrown(BadRequestException e) {
        Assert.assertNotNull(e);
    }

    @Test
    public void createLoanWhenLicenseIsNotConsumed() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsNotConsumedLicense();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenStatusIsNotConsumedLicense() {
        given(status.isConsumedLicense()).willReturn(false);
    }

    @Test
    public void createLoanWhenProductIsNotConsumed() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenFormatDecorationInContentProvider();
        givenConsumeProductResponse();
        givenResult();
        givenStatus();
        givenStatusIsNotConsumedProduct();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenStatusIsNotConsumedProduct() {
        given(status.isConsumedProduct()).willReturn(false);
    }

    @Test
    public void createLoanWhenNoConsumedProductFormats() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenFormatDecorationInContentProvider();
        givenConsumeProductResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedProduct();
        givenConsumedProduct();
        givenFormatIdFromFormatDecoration();
        try {
            whenCreateLoan();
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    @Test
    public void createLoanWhenNotSameFormat() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenFormatDecorationInContentProvider();
        givenConsumeProductResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedProduct();
        givenConsumedProduct();
        givenConsumedProductFormats();
        givenFormatIdFromFormatDecoration();
        givenIsNotSameFormat();
        try {
            whenCreateLoan();
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    private void givenIsNotSameFormat() {
        given(consumedProductFormat.isSameFormat(any(String.class))).willReturn(false);
    }

    @Test
    public void getContent() {
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenContentProviderRecordIdInLoanMetadata();
        givenConsumeProductResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedProduct();
        givenConsumedProduct();
        givenConsumedProductFormats();
        givenFormatIdFromFormatDecoration();
        givenIsSameFormat();
        givenContentUrl();
        givenDownloadableContentDisposition();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    private void givenContentProviderRecordIdInLoanMetadata() {
        given(loanMetadata.getRecordId()).willReturn(RECORD_ID);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ELIBU;
    }
}
