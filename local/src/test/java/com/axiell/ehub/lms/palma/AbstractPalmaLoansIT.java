package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.Test;

import javax.xml.ws.Endpoint;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractPalmaLoansIT<T> extends AbstractPalmaIT {
    private PendingLoan pendingLoan;
    private Patron patron;
    private CheckoutTestAnalysis preCheckoutAnalysis;
    private LmsLoan lmsLoan;

    @Override
    void customSetUp() {
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        patron = new Patron.Builder(DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN).build();
    }

    @Override
    Endpoint makeEndpoint(String palmaUrl) {
        return Endpoint.publish(palmaUrl + "/loans", getPalmaLoansServiceInstance());
    }

    @Test
    public void checkOutTest() {
        whenCheckOutTestExecuted();
        thenActiveLoanReturned();
    }

    @Test
    public void checkOut() {
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
        preCheckoutAnalysis = palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, patron);
    }

    private void whenCheckOutExecuted() {
        Date expirationDate = new Date();
        lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, patron);
    }

    protected abstract T getPalmaLoansServiceInstance();
}
