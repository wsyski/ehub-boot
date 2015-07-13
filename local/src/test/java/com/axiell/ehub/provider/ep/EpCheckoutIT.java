package com.axiell.ehub.provider.ep;

import com.axiell.ehub.util.IFinder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class EpCheckoutIT extends AbstractEpIT {

    private CheckoutDTO checkout;

    @Ignore
    @Test
    public void checkout() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
        givenContentProvider();
        givenEhubConsumer();
        whenCheckout(FORMAT_ID);
        thenPatronHasCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void whenCheckout(final String contentProviderFormatId) {
        checkout = underTest.checkout(contentProviderConsumer, patron, RECORD_ID, contentProviderFormatId);
    }

    private void thenPatronHasCheckout() throws IFinder.NotFoundException {
        checkout = underTest.getCheckout(contentProviderConsumer, patron, checkout.getId());
    }

    private void thenCheckoutHasTransactionId() {
        assertNotNull(checkout.getId());
    }

    private void thenCheckoutHasExpirationDate() {
        assertNotNull(checkout.getExpirationDate());
    }

    private void thenCheckoutHasDownloadUrl() {
        assertNotNull(checkout.getContentUrl());
    }


}
