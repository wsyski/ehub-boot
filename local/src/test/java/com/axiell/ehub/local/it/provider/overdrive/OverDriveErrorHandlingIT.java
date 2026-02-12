package com.axiell.ehub.local.it.provider.overdrive;

import com.axiell.ehub.common.EhubAssert;
import com.axiell.ehub.common.InternalServerErrorException;
import com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

@Disabled
public class OverDriveErrorHandlingIT extends AbstractOverDriveIT {

    @Test
    public void getProductFailed() {
        givenOAuthAccessToken();
        givenInvalidLibraryId();
        givenApiBaseUrl();
        try {
            whenGetProduct();
            Assertions.fail("An InternalServerErrorException should have been thrown");
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
            Assertions.fail("An InternalServerErrorException should have been thrown");
        } catch (InternalServerErrorException e) {
            EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
        }
    }

    private void whenInvalidCheckout() {
        underTest.checkout(contentProviderConsumer, accessToken, INVALID_VALUE, FORMAT_TYPE);
    }
}
