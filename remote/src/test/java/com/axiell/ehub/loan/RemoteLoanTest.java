/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.*;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.Locale;

import static org.junit.Assert.*;

/**
 *
 */
public class RemoteLoanTest extends AbstractEhubClientTest<LoanDevelopmentData> {

    @Test
    public void testGetFormatsException() {
        EhubClient ehubClient = null;
        if (ehubService instanceof EhubClient) {
            ehubClient = (EhubClient) ehubService;
        } else {
            Advised advised = (Advised) ehubService;
            try {
                ehubClient = (EhubClient) advised.getTargetSource().getTarget();
            } catch (Exception ex) {
                fail("Can not retrieve EhubClient");
            }
        }
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"failConfig.xml"});
                ehubService = (IEhubService) springContext.getBean("ehubClient");
        try {
            ehubService.getFormats(authInfoNoCard, ContentProviderName.ELIB.toString(), DevelopmentData.ELIB_RECORD_0_ID, Locale.ENGLISH.getLanguage());
        } catch (EhubException ex) {
            // Expected
            Assert.assertEquals(ex.getEhubError().getCause(), ErrorCause.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: mock the ContentProvider layer and fix this test

    /**
     * Test method for {@link IEhubService#createLoan(com.axiell.ehub.security.AuthInfo, PendingLoan)}
     *
     * @throws EhubException
     */
    @Test
    public void testCreateLoan() throws EhubException {
        if (isOnline()) {
            PendingLoan pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.toString(), DevelopmentData.ELIB_RECORD_0_ID,
                    DevelopmentData.ELIB_FORMAT_0_ID);
            ReadyLoan actReadyLoan = ehubService.createLoan(authInfo, pendingLoan);
            assertNotNull(actReadyLoan);
        }
    }

    // TODO: mock the ContentProvider layer and fix this test

    /**
     * Test method for {@link IEhubService#createLoan(com.axiell.ehub.security.AuthInfo, PendingLoan)}
     *
     * @throws EhubException
     */
    @Test
    public void testGetFormats() throws EhubException {
        if (isOnline()) {
            final Formats formats =
                    ehubService.getFormats(authInfoNoCard, ContentProviderName.ELIB.toString(), DevelopmentData.ELIB_RECORD_0_ID, Locale.ENGLISH.getLanguage());
            assertNotNull(formats);
        }
    }

    // TODO: mock the ContentProvider layer and fix this test
//    /**
//     * Test method for {@link IEhubService#getReadyLoan(com.axiell.ehub.security.AuthInfo, Long)}.
//     * @throws EhubException
//     */
//    @Test
//    public void testGetReadyLoanAuthInfoLong() throws EhubException {
//        ReadyLoan actReadyLoan = ehubService.getReadyLoan(authInfo, developmentData.getEhubLoanId());
//        assertNotNull(actReadyLoan);
//        LmsLoan actLmsLoan = actReadyLoan.getLmsLoan();
//        assertNotNull(actLmsLoan);
//        assertEquals(DevelopmentData.LMS_LOAN_ID, actLmsLoan.getId());
//        ContentProviderLoan actContentProviderLoan = actReadyLoan.getContentProviderLoan();
//        assertNotNull(actContentProviderLoan);
//        ContentProviderLoanMetadata actContentProviderLoanMetadata = actContentProviderLoan.getMetadata();
//        assertNotNull(actContentProviderLoanMetadata);
//        assertEquals(DevelopmentData.CONTENT_PROVIDER_LOAN_ID, actContentProviderLoanMetadata.getId());
//    }

    /**
     *
     */
    @Test
    public void testUnknownEhubLoanId() {
        try {
            ehubService.getReadyLoan(authInfo, Long.MAX_VALUE);
            fail("An EhubException should have been thrown");
        } catch (EhubException e) {
            assertNotNull(e);
            EhubError ehubError = e.getEhubError();
            assertNotNull(ehubError);
            ErrorCause actCause = ehubError.getCause();
            assertEquals(ErrorCause.LOAN_BY_ID_NOT_FOUND, actCause);
        }

    }

    // TODO: mock the ContentProvider layer and fix this test
//    /**
//     * Test method for
//     * {@link com.axiell.ehub.loan.RemoteLoanTest#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.String)}.
//     * @throws EhubException
//     */
//    @Test
//    public void testGetReadyLoanAuthInfoString() throws EhubException {
//        ReadyLoan actReadyLoan = ehubService.getReadyLoan(authInfo, DevelopmentData.LMS_LOAN_ID);
//        assertNotNull(actReadyLoan);
//        LmsLoan actLmsLoan = actReadyLoan.getLmsLoan();
//        assertNotNull(actLmsLoan);
//        assertEquals(DevelopmentData.LMS_LOAN_ID, actLmsLoan.getId());
//        ContentProviderLoan actContentProviderLoan = actReadyLoan.getContentProviderLoan();
//        assertNotNull(actContentProviderLoan);
//        ContentProviderLoanMetadata actContentProviderLoanMetadata = actContentProviderLoan.getMetadata();
//        assertNotNull(actContentProviderLoanMetadata);
//        assertEquals(DevelopmentData.CONTENT_PROVIDER_LOAN_ID, actContentProviderLoanMetadata.getId());
//    }

    /**
     *
     */
    @Test
    public void testUnknownLmsLoanId() {
        try {
            ehubService.getReadyLoan(authInfo, "unknownLmsLoanId");
            fail("An EhubException should have been thrown");
        } catch (EhubException e) {
            assertNotNull(e);
            EhubError ehubError = e.getEhubError();
            assertNotNull(ehubError);
            ErrorCause actCause = ehubError.getCause();
            assertEquals(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, actCause);
        }
    }

    /**
     * @see com.axiell.ehub.AbstractEhubClientTest#initDevelopmentData(org.springframework.web.context.support.XmlWebApplicationContext,
     *      com.axiell.ehub.provider.IContentProviderAdminController,
     *      com.axiell.ehub.provider.record.format.IFormatAdminController, com.axiell.ehub.consumer.IConsumerAdminController)
     */
    @Override
    protected LoanDevelopmentData initDevelopmentData(XmlWebApplicationContext applicationContext,
                                                      IContentProviderAdminController contentProviderAdminController,
                                                      IFormatAdminController formatAdminController,
                                                      IConsumerAdminController consumerAdminController) {

        IEhubLoanRepository ehubLoanRepository = applicationContext.getBean(IEhubLoanRepository.class);
        assertNotNull(ehubLoanRepository);
        return new LoanDevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, ehubLoanRepository);
    }
}
