/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
import com.axiell.ehub.local.it.DevelopmentData;
import com.axiell.ehub.local.language.ILanguageAdminController;
import com.axiell.ehub.local.provider.IContentProviderAdminController;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import com.axiell.ehub.local.provider.record.platform.IPlatformAdminController;

import java.util.Date;

/**
 *
 */
public class LoanDevelopmentData extends DevelopmentData {
    private IEhubLoanRepository ehubLoanRepository;
    private Long elibehubLoanId;

    public LoanDevelopmentData(final IContentProviderAdminController contentProviderAdminController,
                               final IFormatAdminController formatAdminController,
                               final IConsumerAdminController consumerAdminController,
                               final IEhubLoanRepository ehubLoanRepository,
                               final ILanguageAdminController languageAdminController,
                               final IPlatformAdminController platformAdminController) {
        super(contentProviderAdminController, formatAdminController, consumerAdminController, languageAdminController, platformAdminController);
        this.ehubLoanRepository = ehubLoanRepository;
    }

    /**
     * @see DevelopmentData#init()
     */
    @Override
    public void init() throws Exception {
        super.init();
        initELibEhubLoan(getEhubConsumer(), getContentProvider());
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
     * @param ehubConsumer
     * @param elibProvider
     */
    private void initELibEhubLoan(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        FormatDecoration elibFormatDecoration1 = elibProvider.getFormatDecoration(DevelopmentData.TEST_EP_FORMAT_1_ID);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata.Builder(elibProvider, new Date(),
                DevelopmentData.TEST_EP_RECORD_1_ID, elibFormatDecoration1).contentProviderLoanId(DevelopmentData.CONTENT_PROVIDER_LOAN_ID).build();
        LmsLoan lmsLoan = new LmsLoan(DevelopmentData.LMS_LOAN_ID);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        elibehubLoanId = ehubLoan.getId();
    }
}
