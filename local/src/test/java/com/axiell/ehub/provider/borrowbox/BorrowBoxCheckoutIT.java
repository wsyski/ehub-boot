package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.IFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BorrowBoxCheckoutIT extends AbstractBorrowBoxIT {
    protected static final String CARD = "4100000009";

    private String contentProviderRecordId;
    private CheckoutDTO checkout;

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EAUDIO);
        whenCheckout(FORMAT_ID_EAUDIO);
        thenPatronHasCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    @Test
    public void eBook() throws IFinder.NotFoundException {
        givenPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenCheckout(FORMAT_ID_EBOOK);
        thenPatronHasCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void givenPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
    }

    private void whenCheckout(final String contentProviderFormatId) {
        checkout = underTest.checkout(contentProviderConsumer, patron, contentProviderRecordId, contentProviderFormatId);
    }

    private void thenPatronHasCheckout() throws IFinder.NotFoundException {
        checkout = underTest.getCheckout(contentProviderConsumer, patron, checkout.getLoanId());
    }

    private void thenCheckoutHasTransactionId() {
        assertNotNull(checkout.getLoanId());
    }

    private void thenCheckoutHasExpirationDate() {
        assertNotNull(checkout.getExpirationDate());
    }

    private void thenCheckoutHasDownloadUrl() {
        assertNotNull(checkout.getContentUrl());
    }


    private void givenContentProviderRecordId(final String contentProviderFormatId) {
        if (FORMAT_ID_EAUDIO.equals(contentProviderFormatId)) {
            contentProviderRecordId = RECORD_ID_EAUDIO;
        } else {
            contentProviderRecordId = RECORD_ID_EBOOK;
        }
    }
}
