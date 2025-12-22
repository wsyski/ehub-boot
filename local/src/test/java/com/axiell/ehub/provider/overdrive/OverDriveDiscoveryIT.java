package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertNotNull(product);
    }

    private void thenProductHasFormats() {
        List<DiscoveryFormatDTO> formats = product.getFormats();
        thenFormatListIsNotNull(formats);
        thenFormatListIsNotEmpty(formats);
        for (DiscoveryFormatDTO format : formats) {
            thenFormatHasId(format);
            thenFormatHasName(format);
        }
    }

    private void thenFormatListIsNotNull(List<DiscoveryFormatDTO> formats) {
        Assertions.assertNotNull(formats);
    }

    private void thenFormatListIsNotEmpty(List<DiscoveryFormatDTO> formats) {
        Assertions.assertFalse(formats.isEmpty());
    }

    private void thenFormatHasId(DiscoveryFormatDTO format) {
        Assertions.assertNotNull(format.getId());
    }

    private void thenFormatHasName(DiscoveryFormatDTO format) {
        Assertions.assertNotNull(format.getName());
    }

    private void thenProductIsAvailable() {
        Assertions.assertTrue(product.isAvailable());
    }

}
