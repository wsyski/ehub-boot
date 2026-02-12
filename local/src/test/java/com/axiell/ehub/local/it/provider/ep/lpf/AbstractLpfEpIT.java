package com.axiell.ehub.local.it.provider.ep.lpf;

import com.axiell.ehub.local.it.provider.ep.AbstractEpIT;
import com.axiell.ehub.local.provider.ep.FormatMetadataDTO;
import com.axiell.ehub.local.provider.ep.lpf.LpfCheckoutDTO;
import com.axiell.ehub.local.provider.ep.lpf.LpfEpFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class AbstractLpfEpIT extends AbstractEpIT<LpfEpFacade, LpfCheckoutDTO> {

    @Test
    public void checkout() {
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
        Assertions.assertNotNull(formatMetadata);
        Assertions.assertNotNull(formatMetadata.getContentLinks());
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
