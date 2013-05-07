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

/**
 * The eHUB client is the only publicly accessible component of the {@link IEhubService}.
 */
public final class EhubClient implements IEhubService {
    private ILoansResource loansResource;
    private IContentProvidersResource contentProvidersResource;


    /**
     * @see com.axiell.ehub.IEhubService#getFormats(com.axiell.ehub.security.AuthInfo, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public Formats getFormats(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) throws
            EhubException {
        final IRecordsResource recordsResource = contentProvidersResource.getRecords(contentProviderName);
        return recordsResource.getFormats(authInfo, contentProviderRecordId, language);
    }

    /**
     * @see com.axiell.ehub.IEhubService#createLoan(com.axiell.ehub.security.AuthInfo, com.axiell.ehub.loan.PendingLoan)
     */
    @Override
    public ReadyLoan createLoan(final AuthInfo authInfo, final PendingLoan pendingLoan) throws EhubException {
        return loansResource.createLoan(authInfo, pendingLoan);
    }

    /**
     * @see com.axiell.ehub.IEhubService#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.Long)
     */
    @Override
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final Long readyLoanId) throws EhubException {
        return loansResource.getLoan(authInfo, readyLoanId);
    }

    /**
     * @see com.axiell.ehub.IEhubService#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.String)
     */
    @Override
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final String lmsLoanId) throws EhubException {
        return loansResource.getLoan(authInfo, lmsLoanId);
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
