package com.axiell.ehub.testdata.it.remote;

import com.axiell.ehub.common.EhubWebApplicationException;
import com.axiell.ehub.common.Fields;
import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.common.checkout.Checkout;
import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.common.checkout.ContentLinks;
import com.axiell.ehub.common.checkout.SupplementLinks;
import com.axiell.ehub.testdata.TestDataConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public abstract class RemoteCheckoutITFixture extends RemoteITFixture {
    protected Fields fields;
    protected String lmsLoanId;
    protected Long readyLoanId;

    @BeforeEach
    public void initFields() {
        fields = new Fields();
        fields.addValue("lmsRecordId", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderAlias", getContentProviderAlias());
        fields.addValue("contentProviderRecordId", TestDataConstants.RECORD_ID_0);
        fields.addValue("issueId", getIssueId());
    }

    @Test
    public final void checkoutWithExistingContentProviderLoan() throws EhubWebApplicationException {
        givenContentProviderFormatId(getContentProviderFormatId());
        givenLmsCheckoutTestActiveLoanResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, getContentProviderFormatId());
        givenLmsCheckoutResponse(TestDataConstants.LMS_RECORD_ID, getContentProviderFormatId());
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout, getContentProviderFormatId(), false);
    }

    @Test
    public final void checkoutWithLmsError() throws EhubWebApplicationException {

        givenContentProviderFormatId(getContentProviderFormatId());
        givenLmsCheckoutTestErrorResponse(isLoanPerProduct(), TestDataConstants.LMS_RECORD_ID, getContentProviderFormatId(), "STORE_ALMA_EXCEPTION");

        Exception exception = Assertions.assertThrows(EhubWebApplicationException.class, () -> {
            Checkout checkout = whenCheckout();
        });
        thenExpectedLmsErrorException(exception, "blockedBorrCard");
    }


    protected void thenValidCheckout(final Checkout checkout, final String contentProviderFormatId, final boolean isNewLoan) {
        Assertions.assertNotNull(checkout);
        thenValidCheckoutMetadata(checkout.metadata(), isNewLoan);
        thenValidSupplementLinks(checkout.supplementLinks(), contentProviderFormatId);
        thenValidContentLinks(checkout.contentLinks(), contentProviderFormatId);
    }

    protected void thenValidCheckoutMetadata(final CheckoutMetadata checkoutMetadata, final boolean isNewLoan) {
        Assertions.assertNotNull(checkoutMetadata);
        Date expirationDate = checkoutMetadata.getExpirationDate();
        Assertions.assertNotNull(expirationDate);
        String lmsLoanId = checkoutMetadata.getLmsLoanId();
        Assertions.assertNotNull(lmsLoanId);
        Long id = checkoutMetadata.getId();
        Assertions.assertNotNull(id);
        Assertions.assertEquals(isNewLoan, checkoutMetadata.isNewLoan());
        if (getIssueId() != null) {
            String issueId = checkoutMetadata.getIssueId();
            Assertions.assertNotNull(issueId);
            Assertions.assertEquals(issueId, getIssueId());
            String issueTitle = checkoutMetadata.getIssueTitle();
            Assertions.assertNotNull(issueTitle);
        }
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

    protected Checkout whenCheckout() throws EhubWebApplicationException {
        Assertions.assertNotNull(fields.getRequiredValue("contentProviderFormatId"));
        IEhubServiceClient ehubServiceClient = getEhubServiceClient();
        return ehubServiceClient.checkout(authInfo, fields, LANGUAGE);
    }

    protected CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubWebApplicationException {
        IEhubServiceClient ehubServiceClient = getEhubServiceClient();
        return ehubServiceClient.findCheckoutByLmsLoanId(authInfo, lmsLoanId, LANGUAGE);
    }

    protected Checkout whenGetCheckoutByLoanId() throws EhubWebApplicationException {
        IEhubServiceClient ehubServiceClient = getEhubServiceClient();
        return ehubServiceClient.getCheckout(authInfo, readyLoanId, LANGUAGE);
    }

    protected abstract String getContentProviderFormatId();

    protected abstract String getIssueId();
}
