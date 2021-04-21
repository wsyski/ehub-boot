package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.Fields;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.authinfo.Patron;

import org.junit.Test;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.ws.Endpoint;
import java.util.Date;
import java.util.Locale;

import static com.axiell.ehub.lms.palma.AbstractPalmaService.BLOCKED_LIBRARY_CARD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractPalmaLoans<T> extends AbstractPalma {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    private PendingLoan pendingLoan;
    private Patron patron;
    private CheckoutTestAnalysis preCheckoutAnalysis;
    private LmsLoan lmsLoan;
    private boolean isLoanPerProduct;

    @Override
    void customSetUp() {
        Fields fields = new Fields().addValue("lmsRecordId", DevelopmentData.LMS_RECORD_ID).addValue("contentProviderAlias", CONTENT_PROVIDER_TEST_EP).addValue(
                "contentProviderRecordId", DevelopmentData.TEST_EP_RECORD_0_ID).addValue("contentProviderFormatId", DevelopmentData.TEST_EP_FORMAT_0_ID);
        pendingLoan = new PendingLoan(fields);
        patron = new Patron.Builder().libraryCard(DevelopmentData.LIBRARY_CARD).pin(DevelopmentData.PIN).build();
        isLoanPerProduct = false;
    }

    @Override
    Endpoint makeEndpoint(String palmaUrl) {
        return Endpoint.publish(palmaUrl + "/loans", getPalmaLoansServiceInstance());
    }

    @Test
    public void checkOutTestOk() {
        whenCheckOutTestExecuted(DevelopmentData.LIBRARY_CARD);
        thenActiveLoanReturned();
    }

    @Test(expected = InternalServerErrorException.class)
    public void checkOutTestError() {
        whenCheckOutTestExecuted(BLOCKED_LIBRARY_CARD);
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

    private void whenCheckOutTestExecuted(final String libraryCard) {
        patron.setLibraryCard(libraryCard);
        preCheckoutAnalysis = palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, patron, isLoanPerProduct, Locale.ENGLISH);
    }

    private void whenCheckOutExecuted() {
        Date expirationDate = new Date();
        lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, patron, isLoanPerProduct, Locale.ENGLISH);
    }

    protected abstract T getPalmaLoansServiceInstance();
}
