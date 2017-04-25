package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.auth.Patron;
import com.axiell.ehub.provider.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.ErrorCauseArgumentType.CHECKOUT_NOT_FOUND;
import static com.axiell.ehub.ErrorCauseArgumentType.CREATE_LOAN_FAILED;

@Component
class OcdCheckoutHandler implements IOcdCheckoutHandler {
    @Autowired
    private IOcdFacade ocdFacade;
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public Checkout checkout(CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String patronId = getPatronId(data);
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final CheckoutDTO checkoutDTO = ocdFacade.checkout(contentProviderConsumer, patronId, contentProviderRecordId);
        if (checkoutDTO == null) {
            throw makeCreateLoanFailedException(data);
        }
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout getCheckout(final CommandData data, final String contentProviderLoanId) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String patronId = getPatronId(data);
        final CheckoutDTO checkoutDTO = ocdFacade.getCheckout(contentProviderConsumer, patronId, contentProviderLoanId);
        if (checkoutDTO == null) {
            final String language = data.getLanguage();
            throw ehubExceptionFactory
                    .createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, CHECKOUT_NOT_FOUND, language);
        }
        return new Checkout(checkoutDTO);
    }

    private InternalServerErrorException makeCreateLoanFailedException(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        return ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, CREATE_LOAN_FAILED, language);
    }

    private String getPatronId(CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        return ocdFacade.getOrCreatePatron(contentProviderConsumer, patron);
    }
}
