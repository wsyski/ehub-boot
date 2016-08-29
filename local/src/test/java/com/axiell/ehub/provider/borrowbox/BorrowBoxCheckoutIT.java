package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.IFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.ClientErrorException;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BorrowBoxCheckoutIT extends AbstractBorrowBoxIT {
    protected static final String LIBRARY_CARD = "ehub1";
    protected static final String INVALID_LIBRARY_CARD = "20126000000002";

    private String contentProviderRecordId;
    private CheckoutDTO checkout;

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenPatron(LIBRARY_CARD);
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
        givenPatron(LIBRARY_CARD);
        givenConfigurationProperties();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenCheckout(FORMAT_ID_EBOOK);
        thenPatronHasCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    @Test(expected = ClientErrorException.class)
    public void invalidCard() throws IFinder.NotFoundException {
        givenPatron(INVALID_LIBRARY_CARD);
        givenConfigurationProperties();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenCheckout(FORMAT_ID_EBOOK);
    }

    private void givenPatron(final String libraryCard) {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(libraryCard);
    }

    private void whenCheckout(final String contentProviderFormatId) {
        checkout = underTest.checkout(contentProviderConsumer, patron, LANGUAGE, contentProviderRecordId, contentProviderFormatId);
    }

    private void thenPatronHasCheckout() throws IFinder.NotFoundException {
        checkout = underTest.getCheckout(contentProviderConsumer, patron, LANGUAGE, checkout.getLoanId());
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
