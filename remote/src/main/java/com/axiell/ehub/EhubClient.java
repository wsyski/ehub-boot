/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.checkout.*;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Required;

/**
 * The eHUB client is the only publicly accessible component of the {@link IEhubService}.
 */
public final class EhubClient implements IEhubService {
    private IRootResource rootResource;

    @Override
    public CheckoutMetadata findCheckoutByLmsLoanId(AuthInfo authInfo, String lmsLoanId, String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts(authInfo);
        SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = checkoutsResource.search(lmsLoanId, language);
        CheckoutsSearchResult checkoutsSearchResult = new CheckoutsSearchResult(searchResultDTO);
        return checkoutsSearchResult.findCheckoutByLmsLoanId(lmsLoanId);
    }

    @Override
    public Checkout getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts(authInfo);
        CheckoutDTO checkoutDTO = checkoutsResource.getCheckout(ehubCheckoutId, language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout checkout(AuthInfo authInfo, Fields fields, String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts(authInfo);
        CheckoutDTO checkoutDTO = checkoutsResource.checkout(fields.toDTO(), language);
        return new Checkout(checkoutDTO);
    }

    @Required
    public void setRootResource(IRootResource rootResource) {
        this.rootResource = rootResource;
    }
}
