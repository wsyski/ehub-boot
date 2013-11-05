package com.axiell.ehub.provider.overdrive;

import static org.mockito.BDDMockito.given;

import org.junit.Assert;
import org.junit.Test;

import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

public class OverDriveErrorHandlingIT extends AbstractOverDriveIT {

    @Test
    public void getProductFailed() {
	givenOAuthAccessToken();
	givenInvalidLibraryId();
	givenApiBaseUrl();
	try {	    
	    whenGetProduct();
	    Assert.fail("An InternalServerErrorException should have been thrown");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }
    
    private void givenInvalidLibraryId() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_LIBRARY_ID)).willReturn(INVALID_VALUE);
    }

    @Test
    public void checkoutFailed() {
	givenPatronAccessToken();
	givenPatronApiBaseUrl();
	givenErrorPageUrl();
	givenReadAuthUrl();
	try {
	    whenInvalidCheckout();
	    Assert.fail("An InternalServerErrorException should have been thrown");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }
    
    private void whenInvalidCheckout() {
	underTest.checkout(contentProviderConsumer, accessToken, INVALID_VALUE, FORMAT_TYPE);
    }
}
