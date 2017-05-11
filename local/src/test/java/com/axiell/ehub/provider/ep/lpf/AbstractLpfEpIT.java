package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.provider.ep.AbstractEpIT;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public abstract class AbstractLpfEpIT extends AbstractEpIT<LpfEpFacade, LpfCheckoutDTO> {

    @Test
    public void checkout()  {
        givenLibraryCardInPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenEhubConsumer();
        whenCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasExpectedDownloadUrls();
        thenPatronHasCheckout();
    }

    private void whenCheckout() {
        checkout = underTest.checkout(contentProviderConsumer, patron, getRecordId(), getFormatIds().get(0));
    }

    private void thenPatronHasCheckout() {
        checkout = underTest.getCheckout(contentProviderConsumer, patron, checkout.getId());
    }

    private void thenCheckoutHasExpectedDownloadUrls() {
        final FormatMetadataDTO formatMetadata = checkout.getFormatMetadata();
        assertNotNull(formatMetadata);
        assertNotNull(formatMetadata.getContentLinks());
    }

    @Override
    protected LpfEpFacade createEpFacade() {
        return new LpfEpFacade();
    }

    @Override
    protected boolean isLoanPerProduct() {
        return false;
    }

}
