package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.CHECKOUT_NOT_FOUND;

@Component
class OcdCheckoutHandler implements IOcdCheckoutHandler {
    @Autowired
    private IOcdFacade ocdFacade;
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public Checkout checkout(BearerToken bearerToken, CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final CheckoutDTO checkoutDTO = ocdFacade.checkout(contentProviderConsumer, bearerToken, contentProviderRecordId);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout getCompleteCheckout(BearerToken bearerToken, CommandData data, String contentProviderLoanId) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final List<CheckoutDTO> checkouts = ocdFacade.getCheckouts(contentProviderConsumer, bearerToken);
        final ICheckoutMatcher matcher = new ContentProviderLoanIdCheckoutMatcher(contentProviderLoanId);
        try {
            final CheckoutDTO checkoutDTO = CheckoutFinder.find(matcher, checkouts);
            return new Checkout(checkoutDTO);
        } catch (CheckoutNotFoundException e) {
            final String language = data.getLanguage();
            throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, CHECKOUT_NOT_FOUND, language);
        }
    }
}
