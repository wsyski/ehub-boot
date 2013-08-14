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
    private Long publitehubLoanId;
    
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
        initPublitEhubLoan(getEhubConsumer(), getPublitProvider());
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
    
    public Long getPublitEhubLoanId() {
        return publitehubLoanId;
    }
    
    /**
     * 
     * @param ehubConsumer
     * @param elibProvider
     */
    private void initELibEhubLoan(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        FormatDecoration elibFormatDecoration1 = elibProvider.getFormatDecoration(DevelopmentData.ELIB_FORMAT_1_ID);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata(DevelopmentData.CONTENT_PROVIDER_LOAN_ID, elibProvider, new Date(),
                elibFormatDecoration1);
        LmsLoan lmsLoan = new LmsLoan(DevelopmentData.LMS_LOAN_ID_1);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        elibehubLoanId = ehubLoan.getId();
    }
    
    private void initPublitEhubLoan(EhubConsumer ehubConsumer, ContentProvider publitProvider) {
        FormatDecoration publitFormatDecoration1 = publitProvider.getFormatDecoration(DevelopmentData.PUBLIT_FORMAT_0_ID);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata(DevelopmentData.CONTENT_PROVIDER_LOAN_ID, publitProvider, new Date(),
                publitFormatDecoration1);
        LmsLoan lmsLoan = new LmsLoan(DevelopmentData.LMS_LOAN_ID_2);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        publitehubLoanId = ehubLoan.getId();
    }
}
