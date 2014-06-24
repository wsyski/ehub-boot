/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.loan.ILoansResource;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Required;

import static com.axiell.ehub.util.EhubUrlCodec.encode;

/**
 * The eHUB client is the only publicly accessible component of the {@link IEhubService}.
 */
public final class EhubClient implements IEhubService {
    private ILoansResource loansResource;
    private IContentProvidersResource contentProvidersResource;

    @Override
    public Formats getFormats(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) throws
            EhubException {
        final String encodedContentProviderName = encode(contentProviderName);
        final IRecordsResource recordsResource = contentProvidersResource.getRecords(encodedContentProviderName);
        return recordsResource.getFormats(authInfo, contentProviderRecordId, language);
    }

    @Override
    public ReadyLoan createLoan(final AuthInfo authInfo, final PendingLoan pendingLoan, final String language) throws EhubException {
        return loansResource.createLoan(authInfo, language, pendingLoan);
    }

    @Override
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final Long readyLoanId, final String language) throws EhubException {
        return loansResource.getLoan(authInfo, readyLoanId, language);
    }

    @Override
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final String lmsLoanId, final String language) throws EhubException {
        return loansResource.getLoan(authInfo, lmsLoanId, language);
    }

    @Required
    public void setContentProvidersResource(final IContentProvidersResource contentProvidersResource) {
        this.contentProvidersResource = contentProvidersResource;
    }

    @Required
    public void setLoansResource(final ILoansResource loansResource) {
        this.loansResource = loansResource;
    }
}
