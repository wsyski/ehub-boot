package com.axiell.ehub.provider.elib.library;

import static com.axiell.ehub.provider.elib.library.ElibUtils.ELIB_DATE_FORMAT;
import static com.axiell.ehub.provider.elib.library.ElibUtils.ELIB_STATUS_CODE_OK;
import static org.mockito.BDDMockito.given;

import java.io.Serializable;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import se.elib.library.orderlist.Response.Data.Orderitem.Book;
import se.elib.library.orderlist.Response.Data.Orderitem.Book.BookData;
import se.elib.library.orderlist.Response.Data.Orderitem.Book.BookData.UrlData;

import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

/**
 * Elib library API integration test.
 */
public class ElibIT extends AbstractContentProviderIT {    
    private static final String ELIB_RETAILER_ID = "926";
    private static final String ELIB_RETAILER_KEY = "hu81K8js";
    private static final String ELIB_PRODUCT_URL = "https://www.elib.se/webservices/GetProduct.asmx/GetProduct";
    private static final String ELIB_RECORD_0_ID = "9100128260";
    private static final String LANGUAGE = "sv";
    private static final String ELIB_CREATE_LOAN_URL = "https://www.elib.se/webservices/createloan.asmx/CreateLoan";
    private static final String ELIB_FORMAT_0_ID = "58";
    private static final String ELIB_LIBRARY_CARD = "909265910";
    private static final String ELIB_LIBRARY_CARD_PIN = "4447";
    private static final String ELIB_ORDER_LIST_URL = "https://www.elib.se/webservices/getlibraryuserorderlist.asmx/GetLibraryUserOrderList";
    
    private IElibFacade underTest;    
    private se.elib.library.product.Response actualProductResponse;
    private se.elib.library.loan.Response actualLoanResponse;
    private se.elib.library.orderlist.Response actualOrderListResponse;
    
    @Before
    public void setUpElibFacade() {
	underTest = new ElibFacade();
    }
    
    @Test
    public void getProduct() {
	givenElibCredentials();
	givenProductUrl();
	whenGetProduct();
	thenProductResponseContainsExpectedElements();
    }
    
    private void givenElibCredentials() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID)).willReturn(ELIB_RETAILER_ID);
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY)).willReturn(ELIB_RETAILER_KEY);
    }
    
    private void givenProductUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.PRODUCT_URL)).willReturn(ELIB_PRODUCT_URL);
    }
    
    private void whenGetProduct() {
	actualProductResponse = underTest.getProduct(contentProviderConsumer, ELIB_RECORD_0_ID, LANGUAGE);
    }
    
    private void thenProductResponseContainsExpectedElements() {
	Assert.assertNotNull(actualProductResponse);
	thenProductResponseContainsStatus101();	
	se.elib.library.product.Response.Data data = thenProductResponseContainsData();
	se.elib.library.product.Response.Data.Product product = thenProductResponseDataContainsProduct(data);
	se.elib.library.product.Response.Data.Product.Formats formats = thenProductContainsFormats(product);
	thenFormatsContainsNonEmptyListOfFormats(formats);
    }
    
    private void thenProductResponseContainsStatus101() {
	se.elib.library.product.Response.Status status = actualProductResponse.getStatus();
	Assert.assertNotNull(status);
	Assert.assertEquals(ELIB_STATUS_CODE_OK, status.getCode());    
    }
    
    private se.elib.library.product.Response.Data thenProductResponseContainsData() {
	se.elib.library.product.Response.Data data = actualProductResponse.getData();
	Assert.assertNotNull(data);
	return data;
    }

    private se.elib.library.product.Response.Data.Product thenProductResponseDataContainsProduct(se.elib.library.product.Response.Data data) {
	se.elib.library.product.Response.Data.Product product = data.getProduct();
	Assert.assertNotNull(product);
	return product;
    }
    
    private se.elib.library.product.Response.Data.Product.Formats thenProductContainsFormats(se.elib.library.product.Response.Data.Product product) {
	se.elib.library.product.Response.Data.Product.Formats formats = product.getFormats();
	Assert.assertNotNull(formats);
	return formats;
    }

    private void thenFormatsContainsNonEmptyListOfFormats(se.elib.library.product.Response.Data.Product.Formats formats) {
	List<se.elib.library.product.Response.Data.Product.Formats.Format> formatList = formats.getFormat();
	Assert.assertNotNull(formatList);
	Assert.assertFalse(formatList.isEmpty());
    }
    
    @Test
    public void createLoan() {
	givenElibCredentials();
	givenCreateLoanUrl();
	whenCreateLoan();
	thenLoanResponseContainsExpectedElements();
    }
    
    private void givenCreateLoanUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.CREATE_LOAN_URL)).willReturn(ELIB_CREATE_LOAN_URL);
    }
    
    private void whenCreateLoan() {
	actualLoanResponse = underTest.createLoan(contentProviderConsumer, ELIB_RECORD_0_ID, ELIB_FORMAT_0_ID, ELIB_LIBRARY_CARD, ELIB_LIBRARY_CARD_PIN);
    }
    
    private void thenLoanResponseContainsExpectedElements() {
	Assert.assertNotNull(actualLoanResponse);
	thenLoanResponseContainsStatus101();
	se.elib.library.loan.Response.Data data = thenLoanResponseContainsData();
	thenLoanResponseDataContainsDownloadUrl(data);
    }
    
    private void thenLoanResponseContainsStatus101() {
	se.elib.library.loan.Response.Status status = actualLoanResponse.getStatus();
	Assert.assertNotNull(status);
	Assert.assertEquals(ELIB_STATUS_CODE_OK, status.getCode());
    }

    private se.elib.library.loan.Response.Data thenLoanResponseContainsData() {
	se.elib.library.loan.Response.Data data = actualLoanResponse.getData();
	Assert.assertNotNull(data);
	return data;
    }
    
    private void thenLoanResponseDataContainsDownloadUrl(se.elib.library.loan.Response.Data data) {
	String url = data.getDownloadurl();
	Assert.assertNotNull(url);
    }
    
    @Test
    public void getOrderList() {
	givenElibCredentials();
	givenOrderListUrl();
	whenGetOrderList();
	thenOrderListResponseContainsExpectedElements();
    }
    
    private void givenOrderListUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.ORDER_LIST_URL)).willReturn(ELIB_ORDER_LIST_URL);
    }
    
    private void whenGetOrderList() {
	actualOrderListResponse = underTest.getOrderList(contentProviderConsumer, ELIB_LIBRARY_CARD);
    }
    
    private void thenOrderListResponseContainsExpectedElements() {
	Assert.assertNotNull(actualOrderListResponse);
	thenOrderListResponseContainsStatus101();
	se.elib.library.orderlist.Response.Data data = thenOrderListResponseContainsData();
	se.elib.library.orderlist.Response.Data.Orderitem orderItem = thenOrderListDataContainsOrderItems(data);
	se.elib.library.orderlist.Response.Data.Orderitem.Book book = thenOrderItemContainsBook(orderItem);
	thenBookContainsRecordId(book);
	thenBookContainsFormat(book);
	thenBookContainsContentUrl(book);
	thenOrderItemContainsLoanExpireDateInExpectedFormat(orderItem);
    }
    
    private void thenOrderListResponseContainsStatus101() {
	se.elib.library.orderlist.Response.Status status = actualOrderListResponse.getStatus();
	Assert.assertNotNull(status);
	Assert.assertEquals(ELIB_STATUS_CODE_OK, status.getCode());
    }
    
    private se.elib.library.orderlist.Response.Data thenOrderListResponseContainsData() {
	se.elib.library.orderlist.Response.Data data = actualOrderListResponse.getData();
	Assert.assertNotNull(data);
	return data;
    }
    
    private se.elib.library.orderlist.Response.Data.Orderitem thenOrderListDataContainsOrderItems(se.elib.library.orderlist.Response.Data data) {
	List<se.elib.library.orderlist.Response.Data.Orderitem> orderItems = data.getOrderitem();
	Assert.assertNotNull(orderItems);
	Assert.assertFalse(orderItems.isEmpty());
	return orderItems.get(0);
    }
    
    private se.elib.library.orderlist.Response.Data.Orderitem.Book thenOrderItemContainsBook(se.elib.library.orderlist.Response.Data.Orderitem orderItem) {
	se.elib.library.orderlist.Response.Data.Orderitem.Book book = orderItem.getBook();
	Assert.assertNotNull(book);
	return book;
    }
    
    private void thenBookContainsRecordId(se.elib.library.orderlist.Response.Data.Orderitem.Book book) {
	String recordId = book.getId();
	Assert.assertNotNull(recordId);
    }
    
    private void thenBookContainsFormat(Book book) {
	Book.Format format = book.getFormat();
	Assert.assertNotNull(format);
    }
    
    private void thenBookContainsContentUrl(Book book) {
	BookData bookData = book.getData();
	Assert.assertNotNull(bookData);
	UrlData urlData = bookData.getData();
	Assert.assertNotNull(urlData);
	List<Serializable> contentList = urlData.getContent();
	Assert.assertNotNull(contentList);
	Assert.assertFalse(contentList.isEmpty());
    }
    
    private void thenOrderItemContainsLoanExpireDateInExpectedFormat(se.elib.library.orderlist.Response.Data.Orderitem orderItem) {
	String loanExpireDate = orderItem.getLoanexpiredate();
	Assert.assertNotNull(loanExpireDate);
	DateTime loanExpireDateTime = ELIB_DATE_FORMAT.parseDateTime(loanExpireDate);
	Assert.assertNotNull(loanExpireDateTime);
    }
}
