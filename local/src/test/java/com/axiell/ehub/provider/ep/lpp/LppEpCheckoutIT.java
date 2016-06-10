package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.AbstractEpIT;
import com.axiell.ehub.provider.ep.EpUserIdValue;
import com.axiell.ehub.provider.ep.IEpFacade;
import com.axiell.ehub.provider.ep.lpf.LpfCheckoutDTO;
import com.axiell.ehub.provider.ep.lpf.LpfEpFacade;
import com.axiell.ehub.util.IFinder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class LppEpCheckoutIT extends AbstractEpIT<LppEpFacade> {

    private LppCheckoutDTO checkout;

    @Ignore
    @Test
    public void checkout() throws IFinder.NotFoundException {
        givenLibraryCardInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
        givenContentProvider();
        givenEhubConsumer();
        whenCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasDownloadUrl();
        thenPatronHasCheckout();
    }

    private void whenCheckout() {
        checkout = underTest.checkout(contentProviderConsumer, patron, RECORD_ID);
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
        assertNotNull(checkout.getFormatMetadatas().get(FORMAT_ID_0));
        assertNotNull(checkout.getFormatMetadatas().get(FORMAT_ID_1));
    }

    @Override
    protected LppEpFacade createEpFacade() {
        return new LppEpFacade();
    }
}
