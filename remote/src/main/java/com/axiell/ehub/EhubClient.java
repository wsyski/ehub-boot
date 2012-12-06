/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.jboss.resteasy.client.ProxyFactory;
import org.springframework.beans.factory.annotation.Required;

import com.axiell.ehub.lms.ILmsResource;
import com.axiell.ehub.lms.record.ExportRecords;
import com.axiell.ehub.lms.record.IndexRecords;
import com.axiell.ehub.loan.ILoansResource;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;

/**
 * The eHUB client is the only publicly accessible component of the {@link IEhubService}.
 */
public final class EhubClient implements IEhubService {
    private String ehubBaseUri;

    /**
     * @see com.axiell.ehub.IEhubService#getFormats(com.axiell.ehub.security.AuthInfo, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Formats getFormats(AuthInfo authInfo, String contentProviderName, String contentProviderRecordId, String language) throws EhubException {
        final IContentProvidersResource contentProvidersResource = ProxyFactory.create(IContentProvidersResource.class, ehubBaseUri);
        final IRecordsResource recordsResource = contentProvidersResource.getRecords(contentProviderName);
        return recordsResource.getFormats(authInfo, contentProviderRecordId, language);
    }

    /**
     * @see com.axiell.ehub.IEhubService#createLoan(com.axiell.ehub.security.AuthInfo, com.axiell.ehub.loan.PendingLoan)
     */
    @Override
    public ReadyLoan createLoan(AuthInfo authInfo, PendingLoan pendingLoan) throws EhubException {
        final ILoansResource loansResource = ProxyFactory.create(ILoansResource.class, ehubBaseUri);
        return loansResource.createLoan(authInfo, pendingLoan);
    }

    /**
     * @see com.axiell.ehub.IEhubService#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.Long)
     */
    @Override
    public ReadyLoan getReadyLoan(AuthInfo authInfo, Long readyLoanId) throws EhubException {
        final ILoansResource loansResource = ProxyFactory.create(ILoansResource.class, ehubBaseUri);
        return loansResource.getLoan(authInfo, readyLoanId);
    }

    /**
     * @see com.axiell.ehub.IEhubService#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.String)
     */
    @Override
    public ReadyLoan getReadyLoan(AuthInfo authInfo, String lmsLoanId) throws EhubException {
        final ILoansResource loansResource = ProxyFactory.create(ILoansResource.class, ehubBaseUri);
        return loansResource.getLoan(authInfo, lmsLoanId);
    }

    /**
     * @see com.axiell.ehub.IEhubService#parseExportRecords(com.axiell.ehub.lms.record.ExportRecords)
     */
    @Override
    public IndexRecords parseExportRecords(ExportRecords exportRecords) throws EhubException {
        final ILmsResource lmsResource = ProxyFactory.create(ILmsResource.class, ehubBaseUri);
        return lmsResource.parseExportRecords(exportRecords);
    }
    
    /**
     * Sets the base URI to the eHUB.
     * 
     * @param ehubBaseUri the base URI to the eHUB to set
     */
    @Required
    public void setEhubBaseUri(String ehubBaseUri) {
        this.ehubBaseUri = ehubBaseUri;
    }
}
