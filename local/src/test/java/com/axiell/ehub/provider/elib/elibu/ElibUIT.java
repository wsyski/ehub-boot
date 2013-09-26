package com.axiell.ehub.provider.elib.elibu;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.provider.elib.elibu.ConsumedProduct.Content;
import com.axiell.ehub.provider.elib.elibu.ConsumedProduct.Format;
import com.axiell.ehub.provider.elib.elibu.Product.AvailableFormat;

/**
 * ElibU integration test.
 */
public class ElibUIT extends AbstractContentProviderIT {
    private static final String ELIBU_SERVICE_ID = "934";
    private static final String ELIBU_SERVICE_KEY = "xvfKOWHNeZF3";
    private static final String ELIBU_PRODUCT_URL = "https://webservices.elib.se/elibu/v1.0/products";
    private static final String ELIBU_RECORD_ID = "1002443";
    private static final String ELIBU_CONSUME_LICENSE_URL = "https://webservices.elib.se/elibu/v1.0/licenses";
    private static final String ELIBU_SUBSCRIPTION_ID = "3";
    private static final String ELIBU_LIBRARY_CARD = "12345";
    private static final Integer EXPECTED_LICENSE_ID = 6;

    private IElibUFacade underTest;   
    private Response actualResponse;
    
    @Before
    public void setUpElibUFacade() {
	underTest = new ElibUFacade();
    }
    
    @Test
    public void getProduct() {
	givenElibUCredentials();
	givenProductUrl();
	whenGetProduct();
	thenGetProductResponseContainsExpectedElements();
    }

    private void givenElibUCredentials() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ELIBU_SERVICE_ID)).willReturn(ELIBU_SERVICE_ID);
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ELIBU_SERVICE_KEY)).willReturn(ELIBU_SERVICE_KEY);
    }
    
    private void givenProductUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.PRODUCT_URL)).willReturn(ELIBU_PRODUCT_URL);
    }
    
    private void whenGetProduct() {
	actualResponse = underTest.getProduct(contentProviderConsumer, ELIBU_RECORD_ID);
    }
    
    private void thenGetProductResponseContainsExpectedElements() {
	Assert.assertNotNull(actualResponse);
	Result result = thenResponseContainsResult();
	Status status = thenResultContainsStatus(result);
	thenStatusIsHasRetrievedProduct(status);
	Product product = thenResultContainsProduct(result);
	thenProductContainsAvailableFormats(product);
    }
    
    private Result thenResponseContainsResult() {
	Result result = actualResponse.getResult();
	Assert.assertNotNull(result);
	return result;
    }
    
    private Status thenResultContainsStatus(Result result) {
	Status status = result.getStatus();
	Assert.assertNotNull(status);
	return status;
    }
    
    private void thenStatusIsHasRetrievedProduct(Status status) {
	Assert.assertTrue(status.hasRetrievedProduct());
    }
    
    private Product thenResultContainsProduct(Result result) {
	Product product = result.getProduct();
	Assert.assertNotNull(product);
	return product;
    }
    
    private void thenProductContainsAvailableFormats(Product product) {
	List<AvailableFormat> availableFormats = product.getFormats();
	Assert.assertNotNull(availableFormats);
	Assert.assertFalse(availableFormats.isEmpty());
    }
    
    @Test
    public void consumeLicense() {
	givenElibUCredentials();
	givenSubscriptionId();
	givenConsumeLicenseUrl();
	whenConsumeLicense();
	thenConsumeLicenseResponseContainsExpectedElements();
    }
    
    private void givenSubscriptionId() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.SUBSCRIPTION_ID)).willReturn(ELIBU_SUBSCRIPTION_ID);
    }
    
    private void givenConsumeLicenseUrl() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.CONSUME_LICENSE_URL)).willReturn(ELIBU_CONSUME_LICENSE_URL);
    }
    
    private void whenConsumeLicense() {
	actualResponse = underTest.consumeLicense(contentProviderConsumer, ELIBU_LIBRARY_CARD);
    }
    
    private void thenConsumeLicenseResponseContainsExpectedElements() {
	Assert.assertNotNull(actualResponse);
	Result result = thenResponseContainsResult();
	Status status = thenResultContainsStatus(result);
	thenStatusIsConsumedLicense(status);
	thenActualLicenseIdEqualsExpectedLicenseId(result);
    }
    
    private void thenStatusIsConsumedLicense(Status status) {
	Assert.assertTrue(status.isConsumedLicense());
    }
    
    private void thenActualLicenseIdEqualsExpectedLicenseId(Result result) {
	License actualLicense = result.getLicense();
	Assert.assertNotNull(actualLicense);
	Integer actualLicenseId = actualLicense.getLicenseId();
	Assert.assertEquals(EXPECTED_LICENSE_ID, actualLicenseId);
    }
    
    @Test
    public void consumeProduct() {
	givenElibUCredentials();
	givenProductUrl();
	whenConsumeProduct();
	thenConsumeProductResponseContainsExpectedElement();
    }
    
    private void whenConsumeProduct() {
	actualResponse = underTest.consumeProduct(contentProviderConsumer, EXPECTED_LICENSE_ID, ELIBU_RECORD_ID);
    }
    
    private void thenConsumeProductResponseContainsExpectedElement() {
	Assert.assertNotNull(actualResponse);
	Result result = thenResponseContainsResult();
	Status status = thenResultContainsStatus(result);
	thenStatusIsConsumedProduct(status);
	thenResultContainsContentUrl(result);
    }
    
    private void thenStatusIsConsumedProduct(Status status) {
	Assert.assertTrue(status.isConsumedProduct());
    }
    
    private void thenResultContainsContentUrl(Result result) {
	ConsumedProduct consumedProduct = result.getConsumedProduct();
	Assert.assertNotNull(consumedProduct);
	List<Format> formats = consumedProduct.getFormats();
	Assert.assertNotNull(formats);
	Assert.assertFalse(formats.isEmpty());
	Format format = formats.get(0);
	Assert.assertNotNull(format);
	Content content = format.getContent();
	Assert.assertNotNull(content);
	String url = content.getUrl();
	Assert.assertNotNull(url);
    }
}
