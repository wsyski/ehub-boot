package com.axiell.ehub.provider.ocd;

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
    private static final String CARD = "12345";
    private static final String PIN = "1111";
    private BearerToken bearerToken;
    private String contentProviderRecordId;
    private CheckoutDTO checkoutDTO;
    private Checkout checkout;

    @Override
    public void customSetUp() {
        newBearerToken();
    }

    private void newBearerToken() {
        givenApiBaseUrl();
        givenBasicToken();
        givenContentProvider();
        givenLibraryId();
        givenCardPin();
        bearerToken = underTest.newBearerToken(contentProviderConsumer, patron);
    }

    private void givenCardPin() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
        given(patron.getPin()).willReturn(PIN);
    }

    @Test
    public void checkout_audio() throws CheckoutNotFoundException {
        givenAudioTitleIdAsContentProviderRecordId();
        whenCheckout();
        thenCheckoutIsSuccessful();
        thenPatronHasCheckoutInListOfCheckouts();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void givenAudioTitleIdAsContentProviderRecordId() {
        contentProviderRecordId = AUDIO_TITLE_ID;
    }

    private void whenCheckout() {
        checkoutDTO = underTest.checkout(contentProviderConsumer, bearerToken, contentProviderRecordId);
        checkout = new Checkout(checkoutDTO);
    }

    private void thenCheckoutIsSuccessful() {
        assertTrue(checkout.isSuccessful());
    }

    private void thenPatronHasCheckoutInListOfCheckouts() throws CheckoutNotFoundException {
        List<CheckoutDTO> checkoutsDTO = underTest.getCheckouts(contentProviderConsumer, bearerToken);
        ContentProviderLoanIdCheckoutMatcher matcher = new ContentProviderLoanIdCheckoutMatcher(checkout.getTransactionId());
        CheckoutDTO foundCheckoutDTO = CheckoutFinder.find(matcher, checkoutsDTO);
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

    @Test
    public void checkout_ebook() throws CheckoutNotFoundException {
        givenEbookTitleIdAsContentProviderRecordId();
        whenCheckout();
        thenCheckoutIsSuccessful();
        thenPatronHasCheckoutInListOfCheckouts();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
    }

    private void givenEbookTitleIdAsContentProviderRecordId() {
        contentProviderRecordId = EBOOK_TITLE_ID;
    }

    @After
//    @Test
    public void checkin() {
        IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        ocdResource.getCheckout(bearerToken, checkout.getTransactionId());
        ocdResource.checkin(bearerToken, checkout.getTransactionId());
//        ocdResource.checkin(bearerToken, "6868800");
    }
}
