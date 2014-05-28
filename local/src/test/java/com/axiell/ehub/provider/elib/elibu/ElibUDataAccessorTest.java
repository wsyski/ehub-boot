/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import static com.axiell.ehub.EhubAssert.thenInternalServerErrorExceptionIsThrown;
import static com.axiell.ehub.EhubAssert.thenNotFoundExceptionIsThrown;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.elib.elibu.ConsumedProduct.Content;
import com.axiell.ehub.provider.elib.elibu.Product.AvailableFormat;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatTextBundle;

/**
 *
 */
public class ElibUDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "1";
    private static final Integer LICENSE_ID = 1;
    private static final String CONTENT_URL = "url";

    private AbstractContentProviderDataAccessor underTest;
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
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "elibUFacade", elibUFacade);
    }

    @Test
    public void getFormats() {
        givenProductResponse();
        givenResult();
        givenStatus();
        givenStatusHasRetrievedProduct();
        givenProduct();
        givenAvailableFormats();
        givenFormatIdInAvailableFormat();
        givenTextBundle();
        givenEhubFormatNameAndDescription();
        whenGetFormats();
        thenFormatSetContainsOneFormat();
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
        List<AvailableFormat> availableFormats = Arrays.asList(availableFormat);
        given(product.getFormats()).willReturn(availableFormats);
    }

    private void givenFormatIdInAvailableFormat() {
        given(availableFormat.getId()).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(contentProviderConsumer, CARD, RECORD_ID, LANGUAGE);
    }

    private void thenFormatSetContainsOneFormat() {
        Set<Format> formatSet = thenFormatSetIsNotNull();
        Assert.assertTrue(formatSet.size() == 1);
    }

    private Set<Format> thenFormatSetIsNotNull() {
        Assert.assertNotNull(actualFormats);
        Set<Format> formatSet = actualFormats.getFormats();
        Assert.assertNotNull(formatSet);
        return formatSet;
    }

    @Test
    public void getFormatsWhenNoFormatId() {
        givenProductResponse();
        givenResult();
        givenStatus();
        givenStatusHasRetrievedProduct();
        givenProduct();
        givenAvailableFormats();
        whenGetFormats();
        thenFormatSetIsEmpty();
    }

    private void thenFormatSetIsEmpty() {
        Set<Format> formatSet = thenFormatSetIsNotNull();
        Assert.assertTrue(formatSet.isEmpty());
    }

    @Test
    public void getFormatsWhenProductHasNotBeenRetrieved() {
        givenProductResponse();
        givenResult();
        givenStatus();
        givenStatusHasNotRetrievedProduct();
        try {
            whenGetFormats();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenStatusHasNotRetrievedProduct() {
        given(status.hasRetrievedProduct()).willReturn(false);
    }

    @Test
    public void createLoan() {
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenRecordIdInPendingLoan();
        givenFormatIdInPendingLoan();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
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
        givenCreatedDownloadableContent();
        whenCreateLoan();
        thenActualLoanContainsDownloadUrl();
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

    private void givenRecordIdInPendingLoan() {
        given(pendingLoan.getContentProviderRecordId()).willReturn(RECORD_ID);
    }

    private void givenFormatIdInPendingLoan() {
        given(pendingLoan.getContentProviderFormatId()).willReturn(FORMAT_ID);
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
        List<ConsumedProduct.Format> formats = Arrays.asList(consumedProductFormat);
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
        given(content.getUrl()).willReturn(CONTENT_URL);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(contentProviderConsumer, CARD, PIN, pendingLoan, LANGUAGE);
    }

    @Test
    public void createLoanWhenMissingFormat() {
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenRecordIdInPendingLoan();
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
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenRecordIdInPendingLoan();
        givenFormatIdInPendingLoan();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
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
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenRecordIdInPendingLoan();
        givenFormatIdInPendingLoan();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
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
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenRecordIdInPendingLoan();
        givenFormatIdInPendingLoan();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
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
        givenConsumeLicenseResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedLicense();
        givenLicenseId();
        givenContentProviderRecordId();
        givenConsumeProductResponse();
        givenResult();
        givenStatus();
        givenStatusIsConsumedProduct();
        givenConsumedProduct();
        givenConsumedProductFormats();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenFormatIdFromFormatDecoration();
        givenIsSameFormat();
        givenContentUrl();
        givenDownloadableContentDisposition();
        givenCreatedDownloadableContent();
        whenGetContent();
        thenActualContentContainsDownloadUrl();
    }

    private void givenContentProviderRecordId() {
        given(loanMetadata.getRecordId()).willReturn(RECORD_ID);
    }

    private void whenGetContent() {
        actualContent = underTest.getContent(contentProviderConsumer, CARD, PIN, loanMetadata, LANGUAGE);
    }
}
