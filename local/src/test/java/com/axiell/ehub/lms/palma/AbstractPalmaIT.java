package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.ws.Endpoint;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/axiell/ehub/common-context.xml")
public abstract class AbstractPalmaIT<T> {
    private Endpoint endpoint;

    @Autowired
    private PalmaDataAccessor palmaDataAccessor;

    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;
    private CheckoutTestAnalysis preCheckoutAnalysis;
    private LmsLoan lmsLoan;

    @Before
    public void setUp() throws Exception {
        ehubConsumer = DevelopmentData.createEhubConsumer();
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        String palmaUrl = ehubConsumer.getProperties().get(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL);
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);
        endpoint = Endpoint.publish(palmaUrl + "/loans", getPalmaLoansServiceInstance());
    }

    @After
    public void tearDown() {
        endpoint.stop();
    }

    @Test
    public void testCheckOutTest() {
        whenCheckOutTestExecuted();
        thenActiveLoanReturned();
    }

    @Test
    public void testCheckOut() {
        whenCheckOutExecuted();
        thenLmsLoanIsReturned();
    }

    private void thenLmsLoanIsReturned() {
        assertNotNull(lmsLoan);
        assertEquals(DevelopmentData.LMS_LOAN_ID_1, lmsLoan.getId());
    }

    private void thenActiveLoanReturned() {
        assertNotNull(preCheckoutAnalysis);
        assertEquals(CheckoutTestAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(DevelopmentData.LMS_LOAN_ID_1, preCheckoutAnalysis.getLmsLoanId());
    }

    private void whenCheckOutTestExecuted() {
        preCheckoutAnalysis =
                palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
    }

    private void whenCheckOutExecuted() {
        Date expirationDate = new Date();
        lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, DevelopmentData.ELIB_LIBRARY_CARD,
                DevelopmentData.ELIB_LIBRARY_CARD_PIN);
    }

    protected abstract T getPalmaLoansServiceInstance();
}
