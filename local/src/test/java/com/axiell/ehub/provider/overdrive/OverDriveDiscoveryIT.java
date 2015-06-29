package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.mockito.BDDMockito.given;

public class OverDriveDiscoveryIT extends AbstractOverDriveIT {
    private static final String LIBRARY_ID = "4425";

    @Test
    public void getProduct() {
        givenOAuthAccessToken();
        givenLibraryId();
        givenApiBaseUrl();
        whenGetProduct();
        thenProductIsNotNull();
        thenProductHasFormats();
        thenProductIsAvailable();
    }

    private void givenLibraryId() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_LIBRARY_ID)).willReturn(LIBRARY_ID);
    }

    private void thenProductIsNotNull() {
        Assert.assertNotNull(product);
    }

    private void thenProductHasFormats() {
        List<DiscoveryFormat> formats = product.getFormats();
        thenFormatListIsNotNull(formats);
        thenFormatListIsNotEmpty(formats);
        for (DiscoveryFormat format : formats) {
            thenFormatHasId(format);
            thenFormatHasName(format);
        }
    }

    private void thenFormatListIsNotNull(List<DiscoveryFormat> formats) {
        Assert.assertNotNull(formats);
    }

    private void thenFormatListIsNotEmpty(List<DiscoveryFormat> formats) {
        Assert.assertFalse(formats.isEmpty());
    }

    private void thenFormatHasId(DiscoveryFormat format) {
        Assert.assertNotNull(format.getId());
    }

    private void thenFormatHasName(DiscoveryFormat format) {
        Assert.assertNotNull(format.getName());
    }

    private void thenProductIsAvailable() {
        Assert.assertTrue(product.isAvailable());
    }

}
