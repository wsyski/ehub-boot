/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.checkout.*;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.RecordDTO;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.authinfo.AuthInfo;
import org.springframework.beans.factory.annotation.Required;

/**
 * The eHUB client is the only publicly accessible component of the {@link IEhubService}.
 */
public final class EhubClient implements IEhubService {
    private IRootResource rootResource;

    @Override
    public CheckoutMetadata findCheckoutByLmsLoanId(AuthInfo authInfo, String lmsLoanId, String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts();
        SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = checkoutsResource.search(authInfo,lmsLoanId, language);
        CheckoutsSearchResult checkoutsSearchResult = new CheckoutsSearchResult(searchResultDTO);
        return checkoutsSearchResult.findCheckoutByLmsLoanId(lmsLoanId);
    }

    @Override
    public Checkout getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts();
        CheckoutDTO checkoutDTO = checkoutsResource.getCheckout(authInfo, ehubCheckoutId, language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout checkout(AuthInfo authInfo, Fields fields, String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts();
        CheckoutDTO checkoutDTO = checkoutsResource.checkout(authInfo, fields.toDTO(), language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Record getRecord(AuthInfo authInfo, String contentProviderAlias, String contentProviderRecordId, String language) throws EhubException {
        IContentProvidersResource contentProvidersResource = rootResource.contentProviders();
        IRecordsResource recordsResource = contentProvidersResource.records(contentProviderAlias);
        RecordDTO recordDTO = recordsResource.getRecord(authInfo, contentProviderRecordId, language);
        return new Record(recordDTO);
    }

    @Required
    public void setRootResource(IRootResource rootResource) {
        this.rootResource = rootResource;
    }
}
