/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.security.AuthInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/axiell/ehub/admin-controller-context.xml", "classpath:/com/axiell/ehub/business-controller-context.xml"})
public class LoanBusinessControllerTest extends AbstractEhubRepositoryTest<LoanDevelopmentData> {

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;

    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IConsumerAdminController consumerAdminController;

    @Autowired
    private IEhubLoanRepository ehubLoanRepository;

    @Autowired
    private ILoanBusinessController loanBusinessController;

    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected LoanDevelopmentData initDevelopmentData() {
        return new LoanDevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, ehubLoanRepository);
    }

    // TODO: mock the ContentProvider layer and fix this test

    /**
     * Test method for {@link LoanBusinessController#createLoan(AuthInfo, PendingLoan)}
     */
    @Test
    public void testElibCreateLoanOnline() throws EhubException {
        if (isOnline()) {
            //String recordId = DevelopmentData.ELIB_RECORD_0_ID;
            //String format= DevelopmentData.ELIB_FORMAT_0_ID;
            String recordId = DevelopmentData.ELIB_RECORD_1_ID;
            String format = DevelopmentData.ELIB_FORMAT_1_ID;
            AuthInfo authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
                    .libraryCard(DevelopmentData.ELIB_LIBRARY_CARD).pin(DevelopmentData.ELIB_LIBRARY_CARD_PIN).build();
            PendingLoan pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), recordId, format);
            ReadyLoan createReadyLoan = loanBusinessController.createLoan(authInfo, pendingLoan);
            Assert.assertNotNull(createReadyLoan);
            ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, createReadyLoan.getLmsLoan().getId());
            IContent content = readyLoan.getContentProviderLoan().getContent();
            String url =
                    content instanceof DownloadableContent ? DownloadableContent.class.cast(content).getUrl() : StreamingContent.class.cast(content).getUrl();
            Assert.assertNotNull(url);
            Assert.assertNotNull(readyLoan);
            Assert.assertEquals(ContentProviderName.ELIB, readyLoan.getContentProviderLoan().getMetadata().getContentProvider().getName());
        }
    }
    
    @Test
    public void testPublitCreateLoanOnline() throws EhubException {
        if (isOnline()) {
            String recordId = DevelopmentData.PUBLIT_RECORD_0_ID;
            String format = DevelopmentData.PUBLIT_FORMAT_0_ID;
            AuthInfo authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
                    .libraryCard(DevelopmentData.PUBLIT_LIBRARY_CARD).pin(DevelopmentData.PUBLIT_LIBRARY_CARD_PIN).build();
            PendingLoan pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.PUBLIT.name(), recordId, format);
            ReadyLoan createReadyLoan = loanBusinessController.createLoan(authInfo, pendingLoan);
            Assert.assertNotNull(createReadyLoan);
            ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, createReadyLoan.getLmsLoan().getId());
            IContent content = readyLoan.getContentProviderLoan().getContent();
            String url =
                    content instanceof DownloadableContent ? DownloadableContent.class.cast(content).getUrl() : StreamingContent.class.cast(content).getUrl();
            Assert.assertNotNull(url);
            Assert.assertNotNull(readyLoan);
            Assert.assertEquals(ContentProviderName.PUBLIT, readyLoan.getContentProviderLoan().getMetadata().getContentProvider().getName());
        }
    }

    /**
     * Test method for
     * {@link com.axiell.ehub.loan.LoanBusinessController#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.Long)}
     * .
     *
     * @throws EhubException
     */
    @Test
    public void testELibGetReadyLoanAuthInfoLongOnline() throws EhubException {
        if (isOnline()) {
            AuthInfo authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
                    .libraryCard(DevelopmentData.ELIB_LIBRARY_CARD).pin(DevelopmentData.ELIB_LIBRARY_CARD_PIN).build();
            Long expReadyLoanId = developmentData.getELibEhubLoanId();
            try {
                ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, expReadyLoanId);
                Assert.fail("A NotFoundException should have been thrown");
            } catch (NotFoundException e) {
                Assert.assertNotNull(e);
            }
        }
    }
    
    @Test
    public void testPublitGetReadyLoanAuthInfoLongOnline() throws EhubException {
        if (isOnline()) {
            AuthInfo authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
                    .libraryCard(DevelopmentData.PUBLIT_LIBRARY_CARD).pin(DevelopmentData.PUBLIT_LIBRARY_CARD_PIN).build();
            Long expReadyLoanId = developmentData.getPublitEhubLoanId();
            try {
                ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, expReadyLoanId);
                Assert.fail("A NotFoundException should have been thrown");
            } catch (NotFoundException e) {
                Assert.assertNotNull(e);
            }
        }
    }
}
