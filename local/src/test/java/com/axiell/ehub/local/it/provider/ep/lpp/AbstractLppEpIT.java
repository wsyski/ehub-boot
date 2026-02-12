package com.axiell.ehub.local.it.provider.ep.lpp;

import com.axiell.ehub.local.it.provider.ep.AbstractEpIT;
import com.axiell.ehub.local.provider.ep.FormatMetadataDTO;
import com.axiell.ehub.local.provider.ep.lpp.LppCheckoutDTO;
import com.axiell.ehub.local.provider.ep.lpp.LppEpFacade;
import jakarta.ws.rs.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;


public abstract class AbstractLppEpIT extends AbstractEpIT<LppEpFacade, LppCheckoutDTO> {

    @Test
    public void checkout() {
        givenLibraryCardInPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenEhubConsumer();
        // checkout=underTest.getCheckout(contentProviderConsumer,patron,"21802");
        // deleteCheckout("21802");
        whenCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasExpectedDownloadUrls();
        thenPatronHasCheckout();
        thenRepeatedCheckoutFails();
    }

    private void whenCheckout() {
        checkout = underTest.checkout(contentProviderConsumer, patron, getRecordId());
    }

    private void thenRepeatedCheckoutFails() {
        Assertions.assertThrows(InternalServerErrorException.class, this::whenCheckout);
    }

    private void thenPatronHasCheckout() {
        checkout = underTest.getCheckout(contentProviderConsumer, patron, checkout.getId());
    }

    private void thenCheckoutHasExpectedDownloadUrls() {
        final Map<String, FormatMetadataDTO> formatMetadatas = checkout.getFormatMetadatas();
        Assertions.assertNotNull(formatMetadatas);
        getFormatIds().forEach(contentProviderFormatId -> {
            Assertions.assertNotNull(formatMetadatas.get(contentProviderFormatId));
        });
    }


    @Override
    protected LppEpFacade createEpFacade() {
        return new LppEpFacade();
    }

    @Override
    protected boolean isLoanPerProduct() {
        return true;
    }

}
