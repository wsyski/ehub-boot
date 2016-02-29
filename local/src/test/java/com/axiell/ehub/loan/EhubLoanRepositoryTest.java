/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests showing the basic usage of {@link IEhubLoanRepository}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/axiell/ehub/admin-controller-context.xml")
public class EhubLoanRepositoryTest extends AbstractEhubRepositoryTest<LoanDevelopmentData> {
    private static final Long INVALID_EHUB_CONSUMER_ID = -1L;

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


    private EhubLoan expectedEhubLoan;
    private FormatDecoration formatDecoration;
    private EhubLoan actualEhubLoan;
    private long actualCount;

    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected LoanDevelopmentData initDevelopmentData() {
        return new LoanDevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, underTest, languageAdminController);
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
        Assert.assertEquals(DevelopmentData.LMS_LOAN_ID, actualEhubLoan.getLmsLoan().getId());
    }

    private void givenExpectedEhubLoan() {
        Iterable<EhubLoan> iterable = underTest.findAll();
        Assert.assertTrue(iterable.iterator().hasNext());
        expectedEhubLoan = iterable.iterator().next();
    }

    private void whenFindLoanByLmsLoanId() {
        EhubConsumer ehubConsumer = expectedEhubLoan.getEhubConsumer();
        actualEhubLoan = underTest.findLoan(ehubConsumer.getId(), DevelopmentData.LMS_LOAN_ID);
    }

    private void thenActualEhubLoanIsNotNull() {
        Assert.assertNotNull(actualEhubLoan);
    }

    @Test
    @Rollback(true)
    public void findLoanByEhubConsumerIdAndReadyLoanId() {
        givenExpectedEhubLoan();
        whenFindLoanByReadyLoanId();
        thenActualEhubLoanIsNotNull();
    }

    private void whenFindLoanByReadyLoanId() {
        EhubConsumer ehubConsumer = expectedEhubLoan.getEhubConsumer();
        actualEhubLoan = underTest.findLoan(ehubConsumer.getId(), expectedEhubLoan.getId());
    }

    @Test
    @Rollback(true)
    public void invalidEhubConsumerId() {
        givenExpectedEhubLoan();
        actualEhubLoan = underTest.findLoan(INVALID_EHUB_CONSUMER_ID, expectedEhubLoan.getId());
        thenActualEhubLoanIsNull();
    }

    private void thenActualEhubLoanIsNull() {
        Assert.assertNull(actualEhubLoan);
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
        formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
    }

    private void whenCountByFormatDecorationId() {
        final Long formatDecorationId = formatDecoration.getId();
        actualCount = underTest.countLoansByFormatDecorationId(formatDecorationId);
    }

    private void thenActualCountIsOne() {
        Assert.assertEquals(1, actualCount);
    }
}
