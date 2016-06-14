package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.provider.ep.AbstractEpIT;
import com.axiell.ehub.provider.ep.EpUserIdValue;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;
import static com.axiell.ehub.ErrorCauseArgumentValue.Type.ALREADY_ON_LOAN;
import static junit.framework.Assert.assertNotNull;

public abstract class AbstractLppEpIT extends AbstractEpIT<LppEpFacade, LppCheckoutDTO> {

    @Ignore
    @Test
    public void checkout() {
        givenLibraryCardInPatron();
        givenConfigurationProperties(EpUserIdValue.LIBRARY_CARD);
        givenContentProvider();
        givenEhubConsumer();
        whenCheckout();
        thenCheckoutHasTransactionId();
        thenCheckoutHasExpirationDate();
        thenCheckoutHasExpectedDownloadUrls();
        thenPatronHasCheckout();
        // thenRepeatedCheckoutFails();
    }

    private void whenCheckout() {
        checkout = underTest.checkout(contentProviderConsumer, patron, getRecordId());
    }

    private void thenRepeatedCheckoutFails() {
        givenExpectedEhubException(ErrorCause.CONTENT_PROVIDER_ERROR.toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, CONTENT_PROVIDER_TEST_EP),
                        new ErrorCauseArgument(CONTENT_PROVIDER_STATUS, ALREADY_ON_LOAN.name())));
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
