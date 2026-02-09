package com.axiell.ehub;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.SearchResultDTO;
import com.axiell.ehub.controller.external.v5_0.provider.IContentProvidersResource;
import com.axiell.ehub.controller.external.v5_0.provider.IRecordsResource;
import com.axiell.ehub.controller.external.v5_0.provider.dto.ContentProvidersDTO;
import com.axiell.ehub.controller.external.v5_0.provider.dto.RecordDTO;
import com.axiell.ehub.provider.alias.AliasMappings;
import com.axiell.ehub.provider.record.Record;
import org.apache.commons.lang3.StringUtils;

public class EhubClient implements IEhubService {

    private IRootResource rootResource;

    @Override
    public boolean isValidAlias(final String alias) {
        if (StringUtils.isBlank(alias)) {
            return false;
        }
        IContentProvidersResource contentProvidersResource = rootResource.getIV5_0_Resource().contentProviders();
        ContentProvidersDTO contentProvidersDTO = contentProvidersResource.root();
        AliasMappings aliasMappings = AliasMappings.fromContentProvidersDTO(contentProvidersDTO);
        return aliasMappings.entrySet().stream().anyMatch(aliasMapEntry -> aliasMapEntry.getValue().stream().anyMatch(value -> value.equalsIgnoreCase(alias)));
    }

    @Override
    public CheckoutMetadata findCheckoutByLmsLoanId(final AuthInfo authInfo, final String lmsLoanId, final String language) {
        ICheckoutsResource checkoutsResource = rootResource.getIV5_0_Resource().checkouts();
        SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = checkoutsResource.search(authInfo, lmsLoanId, language);
        CheckoutsSearchResult checkoutsSearchResult = new CheckoutsSearchResult(searchResultDTO);
        return checkoutsSearchResult.findCheckoutByLmsLoanId(lmsLoanId);
    }

    @Override
    public Checkout getCheckout(final AuthInfo authInfo, final Long ehubCheckoutId, final String language) {
        ICheckoutsResource checkoutsResource = rootResource.getIV5_0_Resource().checkouts();
        CheckoutDTO checkoutDTO = checkoutsResource.getCheckout(authInfo, ehubCheckoutId, language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout checkout(final AuthInfo authInfo, final Fields fields, final String language) {
        ICheckoutsResource checkoutsResource = rootResource.getIV5_0_Resource().checkouts();
        CheckoutDTO checkoutDTO = checkoutsResource.checkout(authInfo, fields.toDTO(), language);
        return new Checkout(checkoutDTO);
    }

    @Override
    public Record getRecord(final AuthInfo authInfo, final String contentProviderAlias, final String contentProviderRecordId, final String language) throws EhubException {
        IContentProvidersResource contentProvidersResource = rootResource.getIV5_0_Resource().contentProviders();
        IRecordsResource recordsResource = contentProvidersResource.getRecordsResource(contentProviderAlias);
        RecordDTO recordDTO = recordsResource.getRecord(authInfo, contentProviderRecordId, language);
        return new Record(recordDTO);
    }

    public void setRootResource(IRootResource rootResource) {
        this.rootResource = rootResource;
    }
}
