package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class OcdCheckoutIT extends AbstractOcdIT {

    private String contentProviderRecordId;
    private Checkout checkout;
    private String patronId;

    @Override
    public void customSetUp() {
        givenContentProvider();
        givenApiBaseUrl();
        givenApiBaseUrl();
        givenLibraryId();
        givenBasicToken();
        PatronDTO patronDTO = underTest.getPatron(contentProviderConsumer, patron);
        patronId = patronDTO.getPatronId();
    }

    @After
    public void checkin() {
        List<CheckoutDTO> checkoutsDTO = underTest.getCheckouts(contentProviderConsumer, patronId);
        for (CheckoutDTO checkoutDTO : checkoutsDTO) {
            underTest.checkin(contentProviderConsumer, patronId, checkoutDTO.getIsbn());
        }
    }

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenContentProviderRecordId(FORMAT_ID_EAUDIO);
        whenCheckout();
        thenPatronHasCheckoutInListOfCheckouts();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    @Test
    public void eBook() throws IFinder.NotFoundException {
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenCheckout();
        thenPatronHasCheckoutInListOfCheckouts();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void whenCheckout() {
        CheckoutDTO checkoutDTO = underTest.checkout(contentProviderConsumer, patronId, contentProviderRecordId);
        checkout = new Checkout(checkoutDTO);
    }

    private void thenPatronHasCheckoutInListOfCheckouts() throws IFinder.NotFoundException {
        List<CheckoutDTO> checkoutsDTO = underTest.getCheckouts(contentProviderConsumer, patronId);
        IMatcher<CheckoutDTO> matcher = new ContentProviderLoanIdCheckoutMatcher(checkout.getTransactionId());
        CheckoutDTO foundCheckoutDTO = new CollectionFinder<CheckoutDTO>().find(matcher, checkoutsDTO);
        checkout = new Checkout(foundCheckoutDTO);
    }

    private void thenCheckoutHasTransactionId() {
        assertNotNull(checkout.getTransactionId());
    }

    private void thenCheckoutHasExpirationDate() {
        assertNotNull(checkout.getExpirationDate());
    }

    private void thenCheckoutHasDownloadUrl() {
        assertNotNull(checkout.getDownloadUrls());
    }

    private void givenContentProviderRecordId(final String contentProviderFormatId) {
        if (FORMAT_ID_EAUDIO.equals(contentProviderFormatId)) {
            contentProviderRecordId = RECORD_ID_EAUDIO;
        } else {
            contentProviderRecordId = RECORD_ID_EBOOK;
        }
    }
}
