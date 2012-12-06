/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * Tests showing the basic usage of {@link IEhubLoanRepository}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/axiell/ehub/admin-controller-context.xml")
public class EhubLoanRepositoryTest extends AbstractEhubRepositoryTest<LoanDevelopmentData> {    
    
    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    
    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IConsumerAdminController consumerAdminController;
    
    @Autowired
    private IEhubLoanRepository ehubLoanRepository;
    
    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected LoanDevelopmentData initDevelopmentData() {
        return new LoanDevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, ehubLoanRepository);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    @Rollback(true)
    public void testGetLoan() throws Exception {
        Iterable<EhubLoan> iterable = ehubLoanRepository.findAll();
        Assert.assertTrue(iterable.iterator().hasNext());
        EhubLoan ehubLoan = iterable.iterator().next();
        EhubConsumer ehubConsumer = ehubLoan.getEhubConsumer();
        Assert.assertEquals(2, ehubConsumer.getProperties().size());
        ehubLoan = ehubLoanRepository.getLoan(ehubConsumer.getId(), DevelopmentData.LMS_LOAN_ID);
        Assert.assertEquals(DevelopmentData.LMS_LOAN_ID, ehubLoan.getLmsLoan().getId());
    }
}
