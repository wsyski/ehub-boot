package com.axiell.ehub.provider.ep;

import com.axiell.ehub.util.IFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EpCheckoutIT extends AbstractEpIT {
    private static final String PATRON_ID = "patronId";

    private CheckoutDTO checkout;

    @Test
    public void checkout() throws IFinder.NotFoundException {
        givenPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenEhubConsumer();
        whenCheckout(FORMAT_ID);
        thenPatronHasCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void givenPatron() {
        given(patron.getId()).willReturn(PATRON_ID);
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
