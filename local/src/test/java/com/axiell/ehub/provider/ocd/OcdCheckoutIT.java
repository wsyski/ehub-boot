package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OcdCheckoutIT extends AbstractOcdIT {
    protected static final String CARD = "4100000010";
    protected static final String PIN = "1111";

    private BearerToken bearerToken;
    private String contentProviderRecordId;
    private Checkout checkout;

    @Override
    public void customSetUp() {
        newBearerToken();
    }

    @After
    public void checkin() {
        IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        List<CheckoutDTO> checkoutsDTO = underTest.getCheckouts(contentProviderConsumer, bearerToken);
        //ocdResource.getCheckout(bearerToken, checkout.getTransactionId());
        for (CheckoutDTO checkoutDTO : checkoutsDTO) {
            ocdResource.checkin(bearerToken, checkoutDTO.getTransactionId());
        }
    }

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenContentProviderRecordId(FORMAT_ID_EAUDIO);
        whenCheckout();
        thenCheckoutIsSuccessful();
        thenPatronHasCheckoutInListOfCheckouts();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    @Test
    public void eBook() throws IFinder.NotFoundException {
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenCheckout();
        thenCheckoutIsSuccessful();
        thenPatronHasCheckoutInListOfCheckouts();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void newBearerToken() {
        givenApiBaseUrl();
        givenBasicToken();
        givenContentProvider();
        givenLibraryId();
        givenPatron();
        bearerToken = underTest.newBearerToken(contentProviderConsumer, patron);
    }

    private void givenPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
        given(patron.getPin()).willReturn(PIN);
    }

    private void whenCheckout() {
        CheckoutDTO checkoutDTO = underTest.checkout(contentProviderConsumer, bearerToken, contentProviderRecordId);
        checkout = new Checkout(checkoutDTO);
    }

    private void thenCheckoutIsSuccessful() {
        assertTrue(checkout.isSuccessful());
    }

    private void thenPatronHasCheckoutInListOfCheckouts() throws IFinder.NotFoundException {
        List<CheckoutDTO> checkoutsDTO = underTest.getCheckouts(contentProviderConsumer, bearerToken);
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
        assertNotNull(checkout.getDownloadUrl());
    }

    private void givenContentProviderRecordId(final String contentProviderFormatId) {
        if (FORMAT_ID_EAUDIO.equals(contentProviderFormatId)) {
            contentProviderRecordId = RECORD_ID_EAUDIO;
        } else {
            contentProviderRecordId = RECORD_ID_EBOOK;
        }
    }
}
