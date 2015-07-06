package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.Fields;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;

import com.axiell.ehub.provider.ContentProvider;
import org.junit.Test;

import javax.xml.ws.Endpoint;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractPalmaLoansIT<T> extends AbstractPalmaIT {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    private PendingLoan pendingLoan;
    private Patron patron;
    private CheckoutTestAnalysis preCheckoutAnalysis;
    private LmsLoan lmsLoan;

    @Override
    void customSetUp() {
        Fields fields = new Fields().addValue("lmsRecordId", DevelopmentData.LMS_RECORD_ID).addValue("contentProviderAlias", CONTENT_PROVIDER_TEST_EP).addValue(
                "contentProviderRecordId", DevelopmentData.TEST_EP_RECORD_0_ID).addValue("contentProviderFormatId", DevelopmentData.TEST_EP_FORMAT_0_ID);
        pendingLoan = new PendingLoan(fields);
        patron = new Patron.Builder(DevelopmentData.LIBRARY_CARD, DevelopmentData.PIN).build();
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
        assertEquals(DevelopmentData.LMS_LOAN_ID, lmsLoan.getId());
    }

    private void thenActiveLoanReturned() {
        assertNotNull(preCheckoutAnalysis);
        assertEquals(CheckoutTestAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(DevelopmentData.LMS_LOAN_ID, preCheckoutAnalysis.getLmsLoanId());
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
