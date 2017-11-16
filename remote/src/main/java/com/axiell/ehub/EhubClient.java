package com.axiell.ehub;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.RecordDTO;
import com.axiell.ehub.search.SearchResultDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

/**
 * The eHUB client is the only publicly accessible component of the {@link IEhubService}.
 */
public final class EhubClient implements IEhubService {
    private IRootResource rootResource;

    @Override
    public boolean isValidAlias(final AuthInfo authInfo, final String alias) {
        if (StringUtils.isBlank(alias)) {
            return false;
        }
        IContentProvidersResource contentProvidersResource = rootResource.contentProviders();
        Set<String> aliases = contentProvidersResource.getAliases(authInfo);
        return aliases.contains(alias);
    }

    @Override
    public CheckoutMetadata findCheckoutByLmsLoanId(final AuthInfo authInfo, final String lmsLoanId, final String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts();
        SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = checkoutsResource.search(authInfo, lmsLoanId, language);
        CheckoutsSearchResult checkoutsSearchResult = new CheckoutsSearchResult(searchResultDTO);
        return checkoutsSearchResult.findCheckoutByLmsLoanId(lmsLoanId);
    }

    @Override
    public Checkout getCheckout(final AuthInfo authInfo, final Long ehubCheckoutId, final String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts();
        CheckoutDTO checkoutDTO = checkoutsResource.getCheckout(authInfo, ehubCheckoutId, language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout checkout(final AuthInfo authInfo, final Fields fields, final String language) {
        ICheckoutsResource checkoutsResource = rootResource.checkouts();
        CheckoutDTO checkoutDTO = checkoutsResource.checkout(authInfo, fields.toDTO(), language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Record getRecord(final AuthInfo authInfo, final String contentProviderAlias, final String contentProviderRecordId, final String language) throws EhubException {
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
