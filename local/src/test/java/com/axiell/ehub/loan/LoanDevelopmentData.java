/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import java.util.Date;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * 
 */
public class LoanDevelopmentData extends DevelopmentData {
    private IEhubLoanRepository ehubLoanRepository;
    private Long elibehubLoanId;

    /**
     * @param contentProviderAdminController
     * @param formatAdminController
     * @param consumerAdminController
     */
    public LoanDevelopmentData(IContentProviderAdminController contentProviderAdminController, IFormatAdminController formatAdminController,
	    IConsumerAdminController consumerAdminController, IEhubLoanRepository ehubLoanRepository) {
	super(contentProviderAdminController, formatAdminController, consumerAdminController);
	this.ehubLoanRepository = ehubLoanRepository;
    }

    /**
     * @see com.axiell.ehub.DevelopmentData#init()
     */
    @Override
    public void init() throws Exception {
	super.init();
	initELibEhubLoan(getEhubConsumer(), getElibProvider());
    }

    /**
     * @see com.axiell.ehub.DevelopmentData#destroy()
     */
    @Override
    public void destroy() throws Exception {
	ehubLoanRepository.deleteAll();
	super.destroy();
    }

    /**
     * Returns the ehubLoanId.
     * 
     * @return the ehubLoanId
     */
    public Long getELibEhubLoanId() {
	return elibehubLoanId;
    }

    /**
     * 
     * @param ehubConsumer
     * @param elibProvider
     */
    private void initELibEhubLoan(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
	FormatDecoration elibFormatDecoration1 = elibProvider.getFormatDecoration(DevelopmentData.ELIB_FORMAT_1_ID);
	ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata.Builder(elibProvider, new Date(),
		DevelopmentData.ELIB_RECORD_1_ID, elibFormatDecoration1).contentProviderLoanId(DevelopmentData.CONTENT_PROVIDER_LOAN_ID).build();
	LmsLoan lmsLoan = new LmsLoan(DevelopmentData.LMS_LOAN_ID_1);
	EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
	ehubLoan = ehubLoanRepository.save(ehubLoan);
	elibehubLoanId = ehubLoan.getId();
    }
}
