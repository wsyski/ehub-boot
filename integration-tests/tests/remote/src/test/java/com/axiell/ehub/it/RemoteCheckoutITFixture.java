package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public abstract class RemoteCheckoutITFixture extends RemoteITFixture {
    protected Fields fields;
    protected String lmsLoanId;
    protected Long readyLoanId;

    @Before
    public void initFields() {
        fields = new Fields();
        fields.addValue("lmsRecordId", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderAlias", getContentProviderAlias());
        fields.addValue("contentProviderRecordId", TestDataConstants.RECORD_ID_0);
        fields.addValue("contentProviderIssueId", getContentProviderIssueId());
    }

    @Test
    public final void checkoutWithExistingContentProviderLoan() throws EhubException {
        givenContentProviderFormatId(getContentProviderFormatId());
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestActiveLoanResponse();
        givenPalmaCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, getContentProviderFormatId(), false);
    }

    @Test
    public final void checkoutWithLmsError() throws EhubException {
        givenExpectedLmsErrorException("blockedBorrCard");
        givenContentProviderFormatId(getContentProviderFormatId());
        givenPalmaLoansWsdl();
        givenCheckoutTestErrorResponse();
        Checkout checkout = whenCheckout();
    }


    protected void thenValidCheckout(final Checkout checkout, final String contentProviderFormatId, final boolean isNewLoan) {
        Assert.assertNotNull(checkout);
        thenValidCheckoutMetadata(checkout.metadata(), isNewLoan);
        thenValidSupplementLinks(checkout.supplementLinks(), contentProviderFormatId);
        thenValidContentLinks(checkout.contentLinks(), contentProviderFormatId);
    }

    protected void thenValidCheckoutMetadata(final CheckoutMetadata checkoutMetadata, final boolean isNewLoan) {
        Assert.assertNotNull(checkoutMetadata);
        Date expirationDate = checkoutMetadata.getExpirationDate();
        Assert.assertNotNull(expirationDate);
        String lmsLoanId = checkoutMetadata.getLmsLoanId();
        Assert.assertNotNull(lmsLoanId);
        Long id = checkoutMetadata.getId();
        Assert.assertNotNull(id);
        Assert.assertEquals(isNewLoan, checkoutMetadata.isNewLoan());
    }

    protected void thenValidContentLinks(final ContentLinks contentLinks, final String contentProviderFormatId) {
    }

    protected void thenValidSupplementLinks(final SupplementLinks supplementLinks, final String contentProviderFormatId) {
    }

    protected void givenLmsLoanId() {
        lmsLoanId = TestDataConstants.LMS_LOAN_ID;
    }

    protected void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }

    protected void givenContentProviderFormatId(final String contentProviderFormatId) {
        fields.addValue("contentProviderFormatId", contentProviderFormatId);
    }

    protected Checkout whenCheckout() throws EhubException {
        Assert.assertNotNull(fields.getRequiredValue("contentProviderFormatId"));
        return underTest.checkout(authInfo, fields, LANGUAGE);
    }

    protected CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubException {
        return underTest.findCheckoutByLmsLoanId(authInfo, lmsLoanId, LANGUAGE);
    }

    protected Checkout whenGetCheckoutByLoanId() throws EhubException {
        return underTest.getCheckout(authInfo, readyLoanId, LANGUAGE);
    }

    protected abstract void givenContentProviderGetCheckoutResponse();
    protected abstract String getContentProviderFormatId();
    protected abstract String getContentProviderIssueId();
}
