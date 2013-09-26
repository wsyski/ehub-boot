package com.axiell.ehub.provider.publit;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.publit.api.Product;
import com.axiell.ehub.provider.publit.api.ShopCustomerOrder;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl;
import com.axiell.ehub.provider.publit.api.ShopOrderUrl.DownloadItem;
import com.axiell.ehub.provider.record.format.Format;

public class PublitDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "formatId";
    private static final Integer SHOP_CUSTOMER_ORDER_ID = 1;
    private static final int ERROR_STATUS = 500;

    private PublitDataAccessor underTest;
    @Mock
    private IPublitFacade publitFacade;
    @Mock
    private IExpirationDateFactory expirationDateFactory;
    @Mock
    private Product product;
    @Mock
    private ShopCustomerOrder shopCustomerOrder;
    @Mock
    private ShopOrderUrl shopOrderUrl;
    @Mock
    private DownloadItem downloadItem;
    @Mock
    private ClientResponseFailure failure;
    @Mock
    private ClientResponse<?> response;

    @Before
    public void setUpPublitDataAccessor() {
	underTest = new PublitDataAccessor();
	ReflectionTestUtils.setField(underTest, "publitFacade", publitFacade);
	ReflectionTestUtils.setField(underTest, "expirationDateFactory", expirationDateFactory);
    }

    @Test
    public void getFormatsWithPublitFormatNameWhenNoTextBundle() {
	givenProducts();
	givenContentProvider();
	givenFormatId();
	whenGetFormats();
	Format format = thenOneFormatIsReturned();
	thenFormatHasPublitFormatName(format);
	thenFormatHasNoDescription(format);
    }

    private void givenProducts() {
	List<Product> products = Arrays.asList(product);
	given(publitFacade.getProduct(contentProviderConsumer, RECORD_ID)).willReturn(products);
    }

    private void givenFormatId() {
	given(product.getType()).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
	actualFormats = underTest.getFormats(contentProviderConsumer, RECORD_ID, LANGUAGE);
    }

    private Format thenOneFormatIsReturned() {
	Assert.assertNotNull(actualFormats);
	Set<Format> formatSet = actualFormats.getFormats();
	Assert.assertFalse(formatSet.isEmpty());
	return formatSet.iterator().next();
    }

    private void thenFormatHasNoDescription(Format format) {
	String description = format.getDescription();
	Assert.assertNull(description);
    }

    private void thenFormatHasPublitFormatName(Format format) {
	String name = format.getName();
	Assert.assertEquals(FORMAT_ID, name);
    }
    
    @Test
    public void getFormatsWithEhubFormatNameAndDescription() {
	givenProducts();
	givenContentProvider();
	givenFormatId();
	givenTextBundle();
	givenEhubFormatNameAndDescription();
	whenGetFormats();
	thenFormatHasEhubFormatNameAndDescription();
    }
    
    @Test
    public void getFormatsWithPublitFormatNameWhenTextBundle() {
	givenProducts();
	givenContentProvider();
	givenFormatId();
	givenTextBundle();
	whenGetFormats();
	Format format = thenOneFormatIsReturned();
	thenFormatHasPublitFormatName(format);
	thenFormatHasNoDescription(format);
    }

    @Test
    public void getFormatsWhenClientResponseFailureIsThrown() {
	givenClientResponseFailureInGetProduct();
	givenClientResponse();
	givenClientResponseStatus();
	try {
	    whenGetFormats();    
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}	
    }
    
    private void givenClientResponseFailureInGetProduct() {
	given(publitFacade.getProduct(contentProviderConsumer, RECORD_ID)).willThrow(failure);
    }
    
    private void givenClientResponse() {
	given(failure.getResponse()).willReturn(response);
    }
    
    private void givenClientResponseStatus() {
	given(response.getStatus()).willReturn(ERROR_STATUS);
    }

    @Test
    public void createLoan() {
	givenPendingLoan();
	givenShopCustomerOrder();
	givenShopCustomerOrderId();
	givenShopOrderUrl();
	givenDownloadItems();
	givenDownloadUrl();
	givenExpirationDate();
	givenContentProvider();
	givenFormatDecorationFromContentProvider();
	givenContentDisposition();	
	whenCreateLoan();
	thenActualLoanContainsDownloadUrl();
    }

    private void givenPendingLoan() {
	given(pendingLoan.getContentProviderRecordId()).willReturn(RECORD_ID);
	given(pendingLoan.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void givenShopCustomerOrder() {
	given(publitFacade.createShopOrder(contentProviderConsumer, RECORD_ID, CARD)).willReturn(shopCustomerOrder);
    }

    private void givenShopCustomerOrderId() {
	given(shopCustomerOrder.getId()).willReturn(SHOP_CUSTOMER_ORDER_ID);
    }

    private void givenShopOrderUrl() {
	given(publitFacade.getShopOrderUrl(contentProviderConsumer, SHOP_CUSTOMER_ORDER_ID.toString())).willReturn(shopOrderUrl);
    }

    private void givenDownloadItems() {
	List<DownloadItem> downloadItems = Arrays.asList(downloadItem);
	given(shopOrderUrl.getDownloadItems()).willReturn(downloadItems);
    }

    private void givenDownloadUrl() {
	given(downloadItem.getUrl()).willReturn(DOWNLOAD_URL);
    }

    private void whenCreateLoan() {
	actualLoan = underTest.createLoan(contentProviderConsumer, CARD, PIN, pendingLoan);
    }
    
    private void givenExpirationDate() {
	given(expirationDateFactory.createExpirationDate(contentProvider)).willReturn(new Date());
    }

    @Test
    public void createLoanWhenClientResponseFailureIsThrown() {
	givenPendingLoan();
	givenClientResponseFailureInCreateShopOrder();
	givenClientResponse();
	givenClientResponseStatus();
	try {
	    whenCreateLoan();    
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}	
    }

    private void givenClientResponseFailureInCreateShopOrder() {
	given(publitFacade.createShopOrder(contentProviderConsumer, RECORD_ID, CARD)).willThrow(failure);
    }
    
    @Test
    public void getContent() {
	givenContentProviderLoanId();
	givenShopOrderUrl();
	givenDownloadItems();
	givenDownloadUrl();
	givenContentProvider();
	givenFormatDecorationFromContentProviderLoanMetadata();
	givenContentDisposition();
	whenGetContent();
	thenActualContentContainsDownloadUrl();
    }

    private void givenContentProviderLoanId() {
	given(loanMetadata.getId()).willReturn(SHOP_CUSTOMER_ORDER_ID.toString());
    }

    private void whenGetContent() {
	actualContent = underTest.getContent(contentProviderConsumer, CARD, PIN, loanMetadata);
    }
    
    @Test
    public void getContentWhenEmptyListOfDownloadItems() {
	givenContentProviderLoanId();
	givenShopOrderUrl();
	givenEmptyListOfDownloadItems();
	try {
	    whenGetContent();
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void givenEmptyListOfDownloadItems() {
	List<DownloadItem> downloadItems = new ArrayList<>();
	given(shopOrderUrl.getDownloadItems()).willReturn(downloadItems);
    }
    
    @Test
    public void getContentWhenClientResponseFailureIsThrown() {
	givenContentProviderLoanId();
	givenClientResponseFailureInGetShopOrderUrl();
	givenClientResponse();
	givenClientResponseStatus();
	try {
	    whenGetContent();
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void givenClientResponseFailureInGetShopOrderUrl() {
	given(publitFacade.getShopOrderUrl(contentProviderConsumer, SHOP_CUSTOMER_ORDER_ID.toString())).willThrow(failure);
    }
}
