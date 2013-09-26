package com.axiell.ehub.provider.publit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.provider.publit.ShopOrderUrl.DownloadItem;

public class PublitIT extends AbstractContentProviderIT {
    private static final String PUBLIT_RECORD_0_ID = "9789174376838";
    private static final String PUBLIT_USERNAME = "axiell";
    private static final String PUBLIT_PASSWORD = "4x1eLl_12";
    private static final String PUBLIT_URL = "http://beta.publit.se/";
    private static final String PUBLIT_LIBRARY_CARD = "12345";
    private static final String SHOP_ORDER_ID = "52593";
    private IPublitFacade underTest;
   
    private List<Product> actualProducts;
    private ShopCustomerOrder actualShopCustomerOrder;
    private ShopOrderUrl actualShopOrderUrl;

    @Before
    public void setUpPublitFacade() {
	underTest = new PublitFacade();
    }
    
    @Test
    public void getProduct() {
	givenPublitCredentials();
	givenProductUrl();
	whenGetProduct();
	thenProductsContainsFormatType();
    }

    private void givenPublitCredentials() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.PUBLIT_USERNAME)).willReturn(PUBLIT_USERNAME);
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.PUBLIT_PASSWORD)).willReturn(PUBLIT_PASSWORD);
    }
    
    private void givenProductUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.PRODUCT_URL)).willReturn(PUBLIT_URL);
    }
    
    private void whenGetProduct() {
	actualProducts = underTest.getProduct(contentProviderConsumer, PUBLIT_RECORD_0_ID);
    }
    
    private void thenProductsContainsFormatType() {
	Assert.assertNotNull(actualProducts);
	Assert.assertFalse(actualProducts.isEmpty());
	Product product = actualProducts.get(0);
	Assert.assertNotNull(product);
	String type = product.getType();
	Assert.assertNotNull(type);
    }

    @Test
    public void createShopOrder() {
	givenPublitCredentials();
	givenCreateLoanUrl();
	whenCreateShopOrder();
	thenShopCustomerOrderContainsId();
    }
    
    private void givenCreateLoanUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.CREATE_LOAN_URL)).willReturn(PUBLIT_URL);
    }
    
    private void whenCreateShopOrder() {
	actualShopCustomerOrder = underTest.createShopOrder(contentProviderConsumer, PUBLIT_RECORD_0_ID, PUBLIT_LIBRARY_CARD);
    }
    
    private void thenShopCustomerOrderContainsId() {
	Assert.assertNotNull(actualShopCustomerOrder);
	Integer id = actualShopCustomerOrder.getId();
	Assert.assertNotNull(id);	
    }
    
    @Test
    public void getShopOrderUrl() {
	givenPublitCredentials();
	givenOrderListUrl();
	whenGetShopOrderUrl();
	thenShopOrderUrlContainsContentUrl();
    }
    
    private void givenOrderListUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.ORDER_LIST_URL)).willReturn(PUBLIT_URL);
    }
    
    private void whenGetShopOrderUrl() {
	actualShopOrderUrl = underTest.getShopOrderUrl(contentProviderConsumer, SHOP_ORDER_ID);
    }
    
    private void thenShopOrderUrlContainsContentUrl() {
	Assert.assertNotNull(actualShopOrderUrl);
	List<DownloadItem> downloadItems = actualShopOrderUrl.getDownloadItems();
	Assert.assertNotNull(downloadItems);
	Assert.assertFalse(downloadItems.isEmpty());
	DownloadItem downloadItem = downloadItems.get(0);
	Assert.assertNotNull(downloadItem);
	String contentUrl = downloadItem.getUrl();
	Assert.assertNotNull(contentUrl);
    }
}
