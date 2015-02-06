/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.record.format.Format;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import se.elib.library.orderlist.Response.Data.Orderitem;
import se.elib.library.product.Response.Data.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.axiell.ehub.EhubAssert.thenInternalServerErrorExceptionIsThrown;
import static com.axiell.ehub.EhubAssert.thenNotFoundExceptionIsThrown;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

/**
 * Elib Data Accessor Test
 */
public class ElibDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final short STATUS_CODE_OK = 101;
    private static final short STATUS_CODE_NOT_OK = 0;
    private static final String ELIB_FORMAT_NAME = "elibFormatName";
    private static final String ELIB_FORMAT_DESCRIPTION = "elibFormatDescription";
    private static final String CONTENT_PROVIDER_LOAN_ID = "1";
    private static final short ELIB_PRODUCT_OK_ID = 9;
    private static final short ELIB_PRODUCT_NOT_OK_ID = 8;

    private ElibDataAccessor underTest;
    @Mock
    private IElibFacade elibFacade;

    @Mock
    private se.elib.library.product.Response productResponse;
    @Mock
    private se.elib.library.product.Response.Data productData;
    @Mock
    private se.elib.library.product.Response.Data.Product product;
    @Mock
    private se.elib.library.product.Response.Data.Product.Formats elibFormats;
    @Mock
    private se.elib.library.product.Response.Data.Product.Formats.Format elibFormat;

    @Mock
    private se.elib.library.loan.Response loanResponse;
    @Mock
    private se.elib.library.orderlist.Response orderListResponse;
    @Mock
    private se.elib.library.orderlist.Response.Data orderListData;
    @Mock
    private se.elib.library.orderlist.Response.Data.Orderitem orderitem;
    @Mock
    private se.elib.library.orderlist.Response.Data.Orderitem.Book book;
    @Mock
    private se.elib.library.orderlist.Response.Data.Orderitem.Book.Format bookFormat;
    @Mock
    private se.elib.library.loan.Response.Data loanData;

    @Mock
    private se.elib.library.orderlist.Response.Data.Orderitem.Book.BookData bookData;
    @Mock
    private se.elib.library.orderlist.Response.Data.Orderitem.Book.BookData.UrlData urlData;
    @Mock
    private Product.Status productStatus;

    @Before
    public void setUpElibDataAccessor() {
        underTest = new ElibDataAccessor();
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "elibFacade", elibFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
    }

    @Test
    public void getFormatsWhenNoDataInProductReponse() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenProductResponse();
        givenProductResponseStatusOk();
        whenGetFormats();
        thenNoFormats();
    }

    private void givenProductResponse() {
        given(elibFacade.getProduct(contentProviderConsumer, RECORD_ID, LANGUAGE)).willReturn(productResponse);
    }

    private void givenProductResponseStatusOk() {
        se.elib.library.product.Response.Status status = new se.elib.library.product.Response.Status();
        status.setCode(STATUS_CODE_OK);
        given(productResponse.getStatus()).willReturn(status);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    private void thenNoFormats() {
        Assert.assertTrue(actualFormats.getFormats().isEmpty());
    }

    private void givenProductFormats() {
        List<se.elib.library.product.Response.Data.Product.Formats.Format> elibFormatList = Arrays.asList(elibFormat);
        given(productResponse.getData()).willReturn(productData);
        given(productData.getProduct()).willReturn(product);
        given(product.getFormats()).willReturn(elibFormats);
        given(elibFormats.getFormat()).willReturn(elibFormatList);
        given(elibFormat.getName()).willReturn(ELIB_FORMAT_NAME);
        given(elibFormat.getDescription()).willReturn(ELIB_FORMAT_DESCRIPTION);
    }

    private void givenNoTextBundle() {
        givenContentProvider();
    }

    private void thenFormatHasElibFormatNameAndDescription() {
        Assert.assertFalse(actualFormats.getFormats().isEmpty());
        Format actualFormat = actualFormats.getFormats().iterator().next();
        Assert.assertEquals(ELIB_FORMAT_NAME, actualFormat.name());
        Assert.assertEquals(ELIB_FORMAT_DESCRIPTION, actualFormat.description());
    }

    private void givenProductStatusIsOk() {
        givenThereIsAProductStatus();
        given(productStatus.getId()).willReturn(ELIB_PRODUCT_OK_ID);
    }

    private void givenProductStatusIsNotOk() {
        givenThereIsAProductStatus();
        given(productStatus.getId()).willReturn(ELIB_PRODUCT_NOT_OK_ID);
    }

    private void givenThereIsAProductStatus() {
        given(product.getStatus()).willReturn(productStatus);
    }

    @Test
    public void getFormatsWithEhubFormatNameAndDescription() {
        givenFormatFromFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenProductResponse();
        givenProductResponseStatusOk();
        givenProductFormats();
        givenProductStatusIsOk();
        givenTextBundle();
        whenGetFormats();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void getFormatsProductIdNotOk() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenProductResponse();
        givenProductResponseStatusOk();
        givenProductFormats();
        givenProductStatusIsNotOk();
        givenTextBundle();
        whenGetFormats();
        thenNoFormats();
    }

    @Test
    public void getFormatsWhenStatusNotOk() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenProductResponse();
        givenProductResponseStatusNotOk();
        try {
            whenGetFormats();
            Assert.fail("An InternalServerErrorException should have been thrown");
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenProductResponseStatusNotOk() {
        se.elib.library.product.Response.Status status = new se.elib.library.product.Response.Status();
        status.setCode(STATUS_CODE_NOT_OK);
        given(productResponse.getStatus()).willReturn(status);
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenContentProvider();
        givenLoanResponse();
        givenLoanResponseStatusOk();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenOrderListData();
        givenOrderItem();
        givenExpectedRecordId();
        givenBookFormat();
        givenExpectedFormatId();
        givenFormatDecorationFromContentProvider();
        givenLoanResponseData();
        givenDownloadUrl();
        givenDownloadableContentDisposition();
        givenCreatedDownloadableContent();
        whenCreateLoan();
        thenActualLoanContainsDownloadUrl();
    }

    private void givenLoanResponse() {
        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(String.class), any(String.class), any(String.class)))
                .willReturn(loanResponse);
    }

    private void givenLoanResponseStatusOk() {
        se.elib.library.loan.Response.Status status = new se.elib.library.loan.Response.Status();
        status.setCode(STATUS_CODE_OK);
        given(loanResponse.getStatus()).willReturn(status);
    }

    private void givenOrderListResponse() {
        given(elibFacade.getOrderList(contentProviderConsumer, CARD)).willReturn(orderListResponse);
    }

    private void givenOrderListResponseStatusOk() {
        se.elib.library.orderlist.Response.Status status = new se.elib.library.orderlist.Response.Status();
        status.setCode(STATUS_CODE_OK);
        given(orderListResponse.getStatus()).willReturn(status);
    }

    private void givenOrderListData() {
        given(orderListResponse.getData()).willReturn(orderListData);
        List<Orderitem> orderItemList = Arrays.asList(orderitem);
        given(orderListData.getOrderitem()).willReturn(orderItemList);
    }

    private void givenOrderItem() {
        given(orderitem.getBook()).willReturn(book);
        given(orderitem.getLoanexpiredate()).willReturn("2013-09-24 13:16:00");
    }

    private void givenExpectedRecordId() {
        given(book.getId()).willReturn(RECORD_ID);
    }

    private void givenBookFormat() {
        given(book.getFormat()).willReturn(bookFormat);
    }

    private void givenExpectedFormatId() {
        given(bookFormat.getId()).willReturn(Short.valueOf(FORMAT_ID));
    }

    private void givenLoanResponseData() {
        given(loanResponse.getData()).willReturn(loanData);
    }

    private void givenDownloadUrl() {
        given(loanData.getDownloadurl()).willReturn(DOWNLOAD_URL);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    @Test
    public void createLoanWhenLoanResponseStatusNotOk() {
        givenPatronInCommandData();
        givenLoanResponse();
        givenLoanResponseStatusNotOk();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenLoanResponseStatusNotOk() {
        se.elib.library.loan.Response.Status status = new se.elib.library.loan.Response.Status();
        status.setCode(STATUS_CODE_NOT_OK);
        given(loanResponse.getStatus()).willReturn(status);
    }

    @Test
    public void createLoanWhenOrderListResponseStatusNotOk() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenContentProvider();
        givenLoanResponse();
        givenLoanResponseStatusOk();
        givenOrderListResponse();
        givenOrderListResponseStatusNotOk();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenOrderListResponseStatusNotOk() {
        se.elib.library.orderlist.Response.Status status = new se.elib.library.orderlist.Response.Status();
        status.setCode(STATUS_CODE_NOT_OK);
        given(orderListResponse.getStatus()).willReturn(status);
    }

    @Test
    public void createLoanWhenNoOrderItems() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenContentProvider();
        givenLoanResponse();
        givenLoanResponseStatusOk();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenEmptyOrderList();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenEmptyOrderList() {
        given(orderListResponse.getData()).willReturn(orderListData);
        List<Orderitem> orderItemList = Arrays.asList();
        given(orderListData.getOrderitem()).willReturn(orderItemList);
    }

    @Test
    public void createLoanWhenNoMatchingRecordIdAndFormatId() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenContentProvider();
        givenLoanResponse();
        givenLoanResponseStatusOk();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenOrderListData();
        givenOrderItem();
        givenUnexpectedRecordId();
        givenBookFormat();
        givenUnexpectedFormatId();
        try {
            whenCreateLoan();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void givenUnexpectedRecordId() {
        given(book.getId()).willReturn("12345");
    }

    private void givenUnexpectedFormatId() {
        given(bookFormat.getId()).willReturn(Short.valueOf("98"));
    }

    @Test
    public void getContent() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenOrderListData();
        givenContentProviderLoanId();
        givenOrderItem();
        givenExpectedOrderId();
        givenBookData();
        givenUrlData();
        givenUrlContent();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenDownloadableContentDisposition();
        givenCreatedDownloadableContent();
        whenGetContent();
        thenActualContentContainsDownloadUrl();
    }

    private void givenContentProviderLoanId() {
        given(loanMetadata.getId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
    }

    private void whenGetContent() {
        actualContent = underTest.getContent(commandData);
    }

    private void givenExpectedOrderId() {
        given(orderitem.getRetailerordernumber()).willReturn(Long.valueOf(CONTENT_PROVIDER_LOAN_ID));
    }

    private void givenBookData() {
        given(book.getData()).willReturn(bookData);
    }

    private void givenUrlData() {
        given(bookData.getData()).willReturn(urlData);
    }

    private void givenUrlContent() {
        List<Serializable> content = new ArrayList<Serializable>();
        content.add(DOWNLOAD_URL);
        given(urlData.getContent()).willReturn(content);
    }

    @Test
    public void getContentWhenNoOrderItems() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenEmptyOrderList();
        givenContentProviderLoanId();
        try {
            whenGetContent();
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    @Test
    public void getContentWhenUnexpectedOrderId() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenOrderListData();
        givenContentProviderLoanId();
        givenOrderItem();
        givenUnexpectedOrderId();
        try {
            whenGetContent();
        } catch (NotFoundException e) {
            thenNotFoundExceptionIsThrown(e);
        }
    }

    private void givenUnexpectedOrderId() {
        given(orderitem.getRetailerordernumber()).willReturn(0L);
    }

    @Test
    public void getContentWhenNoContent() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenPatronInCommandData();
        givenLibraryCardInPatron();
        givenOrderListResponse();
        givenOrderListResponseStatusOk();
        givenOrderListData();
        givenContentProviderLoanId();
        givenOrderItem();
        givenExpectedOrderId();
        givenBookData();
        givenUrlData();
        try {
            whenGetContent();
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionIsThrown(e);
        }
    }
}
