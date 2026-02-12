/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.it.loan;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
import com.axiell.ehub.local.it.AbstractEhubRepositoryIT;
import com.axiell.ehub.local.it.DevelopmentData;
import com.axiell.ehub.local.language.ILanguageAdminController;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.loan.EhubLoan;
import com.axiell.ehub.local.loan.IEhubLoanRepository;
import com.axiell.ehub.local.loan.LoanDevelopmentData;
import com.axiell.ehub.local.provider.IContentProviderAdminController;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import com.axiell.ehub.local.provider.record.platform.IPlatformAdminController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

/**
 * Tests showing the basic usage of {@link IEhubLoanRepository}.
 */
@SpringBootTest(classes = {EhubApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class EhubLoanRepositoryIT extends AbstractEhubRepositoryIT<LoanDevelopmentData> {
    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private IConsumerAdminController consumerAdminController;
    @Autowired
    private IEhubLoanRepository underTest;
    @Autowired
    private ILanguageAdminController languageAdminController;
    @Autowired
    private IPlatformAdminController platformAdminController;
    private EhubLoan expectedEhubLoan;
    private FormatDecoration formatDecoration;
    private EhubLoan actualEhubLoan;
    private long actualCount;

    /**
     * @see AbstractEhubRepositoryIT#initDevelopmentData()
     */
    @Override
    protected LoanDevelopmentData initDevelopmentData() {
        return new LoanDevelopmentData(
                contentProviderAdminController,
                formatAdminController,
                consumerAdminController,
                underTest,
                languageAdminController,
                platformAdminController);
    }

    /**
     * @throws Exception
     */
    @Test
    @Rollback(true)
    public void findLoanByEhubConsumerIdAndLmsLoanId() throws Exception {
        givenExpectedEhubLoan();
        whenFindLoanByLmsLoanId();

        thenActualEhubLoanIsNotNull();
        Assertions.assertEquals(DevelopmentData.LMS_LOAN_ID, actualEhubLoan.getLmsLoan().getId());
    }

    private void givenExpectedEhubLoan() {
        Iterable<EhubLoan> iterable = underTest.findAll();
        Assertions.assertTrue(iterable.iterator().hasNext());
        expectedEhubLoan = iterable.iterator().next();
    }

    private void whenFindLoanByLmsLoanId() {
        EhubConsumer ehubConsumer = expectedEhubLoan.getEhubConsumer();
        actualEhubLoan = underTest.findLoan(ehubConsumer.getId(), DevelopmentData.LMS_LOAN_ID);
    }

    private void thenActualEhubLoanIsNotNull() {
        Assertions.assertNotNull(actualEhubLoan);
    }

    @Test
    @Rollback(true)
    public void findLoanByEhubConsumerIdAndReadyLoanId() {
        givenExpectedEhubLoan();
        whenFindLoanByReadyLoanId();
        thenActualEhubLoanIsNotNull();
    }

    private void whenFindLoanByReadyLoanId() {
        actualEhubLoan = underTest.findById(expectedEhubLoan.getId()).orElse(null);
    }

    @Test
    @Rollback(true)
    public void countByFormatDecoration() {
        givenExpectedEhubLoan();
        givenFormatDecoration();
        whenCountByFormatDecorationId();
        thenActualCountIsOne();
    }

    private void givenFormatDecoration() {
        ContentProviderLoanMetadata contentProviderLoanMetadata = expectedEhubLoan.getContentProviderLoanMetadata();
        formatDecoration = contentProviderLoanMetadata.getFirstFormatDecoration();
    }

    private void whenCountByFormatDecorationId() {
        final Long formatDecorationId = formatDecoration.getId();
        actualCount = underTest.countLoansByFormatDecorationId(formatDecorationId);
    }

    private void thenActualCountIsOne() {
        Assertions.assertEquals(1, actualCount);
    }


}
