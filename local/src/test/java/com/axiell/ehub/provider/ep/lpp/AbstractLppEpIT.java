package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.provider.ep.AbstractEpIT;
import com.axiell.ehub.provider.ep.EpUserIdValue;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import org.junit.Test;

import javax.ws.rs.InternalServerErrorException;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

public abstract class AbstractLppEpIT extends AbstractEpIT<LppEpFacade, LppCheckoutDTO> {

    @Test
    public void checkout() {
        givenLibraryCardInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
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
        givenExpectedWebApplicationException(InternalServerErrorException.class,ErrorCauseArgumentType.ALREADY_ON_LOAN);
        whenCheckout();
    }

    private void thenPatronHasCheckout() {
        checkout = underTest.getCheckout(contentProviderConsumer, patron, checkout.getId());
    }

    private void thenCheckoutHasExpectedDownloadUrls() {
        final Map<String, FormatMetadataDTO> formatMetadatas = checkout.getFormatMetadatas();
        assertNotNull(formatMetadatas);
        getFormatIds().forEach(contentProviderFormatId -> {
            assertNotNull(formatMetadatas.get(contentProviderFormatId));
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
