package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static org.junit.Assert.assertThat;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteLoanIT extends RemoteITFixture {
    private Fields fields;
    private String lmsLoanId;
    private Long readyLoanId;

    @Before
    public void initFields() {
        fields = new Fields();
        fields.addValue("lmsRecordId", TestDataConstants.LMS_RECORD_ID);
        fields.addValue("contentProviderAlias", CONTENT_PROVIDER_ALIAS);
        fields.addValue("contentProviderRecordId", TestDataConstants.TEST_EP_RECORD_0_ID);
        fields.addValue("contentProviderFormatId", TestDataConstants.TEST_EP_FORMAT_0_ID);
    }

    @Test
    public final void checkout() throws EhubException {
        givenPalmaLoansWsdl();
        givenPalmaCheckoutTestOkResponse();
        givenContentProviderCheckoutResponse();
        givenContentProviderGetCheckoutResponse();
        givenPalmaCheckoutResponse();
        Checkout checkout = whenCheckout();
        thenValidCheckout(checkout);
    }

    @Test
    public final void getReadyLoanByReadyLoanId() throws EhubException {
        givenReadyLoanId();
        givenContentProviderGetCheckoutResponse();
        Checkout checkout = whenGetCheckoutByLoanId();
        thenValidCheckout(checkout);
    }

    @Test
    public final void getReadyLoanByLmsLoanId() throws EhubException {
        givenLmsLoanId();
        givenContentProviderGetCheckoutResponse();
        CheckoutMetadata checkoutMetadata = whenFindCheckoutMetadataByLmsLoandId();
        thenValidCheckoutMetadata(checkoutMetadata);
    }

    @Test
    public final void ehubException() {
        givenPalmaLoansWsdl();
        givenCheckoutTestErrorResponse();
        try {
            whenCheckout();
        } catch (EhubException ex) {
            Assert.assertNotNull(ex);
        }
    }

    private void givenContentProviderGetCheckoutResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/checkouts/" + TestDataConstants.CONTENT_PROVIDER_LOAN_ID))
                .willReturn(aResponse().withBodyFile("checkoutResponse.json").withHeader("Content-Type", "application/json").withStatus(201)));
    }

    private void givenContentProviderCheckoutResponse() {
        stubFor(post(urlEqualTo("/ep/api/v1/checkouts"))
                .willReturn(aResponse().withBodyFile("checkoutResponse.json").withHeader("Content-Type", "application/json").withStatus(200)));
    }

    private void thenValidCheckout(final Checkout checkout) {
        Assert.assertNotNull(checkout);
        thenValidCheckoutMetadata(checkout.metadata());
        thenValidSupplementLinks(checkout.supplementLinks());
        thenValidContentLinks(checkout.contentLinks());
    }

    private void thenValidCheckoutMetadata(final CheckoutMetadata checkoutMetadata) {
        Assert.assertNotNull(checkoutMetadata);
        Date expirationDate = checkoutMetadata.expirationDate();
        Assert.assertNotNull(expirationDate);
        String lmsLoanId = checkoutMetadata.lmsLoanId();
        Assert.assertNotNull(lmsLoanId);
        Long id = checkoutMetadata.id();
        Assert.assertNotNull(id);
    }

    private void thenValidContentLinks(final ContentLinks contentLinks) {
        Assert.assertNotNull(contentLinks);
        Assert.assertEquals(2, contentLinks.getContentLinks().size());
        assertThat(contentLinks.getContentLinks().get(0), matchesExpectedContentLink(new ContentLink("http:/localhost:16521/ep/api/v1/records/recordId_0/content_0")));
        assertThat(contentLinks.getContentLinks().get(1), matchesExpectedContentLink(new ContentLink("http:/localhost:16521/ep/api/v1/records/recordId_0/content_1")));
    }

    private void thenValidSupplementLinks(final SupplementLinks supplementLinks) {
        Assert.assertNotNull(supplementLinks);
        Assert.assertEquals(2, supplementLinks.getSupplementLinks().size());
        assertThat(supplementLinks.getSupplementLinks().get(0), matchesExpectedSupplementLink(new SupplementLink("supplent_0", "http:/localhost:16521/ep/api/v1/records/recordId_0/supplement_0")));
        assertThat(supplementLinks.getSupplementLinks().get(1), matchesExpectedSupplementLink(new SupplementLink("supplent_1", "http:/localhost:16521/ep/api/v1/records/recordId_0/supplement_1")));
    }

    private void givenLmsLoanId() {
        lmsLoanId = TestDataConstants.LMS_LOAN_ID;
    }


    private void givenReadyLoanId() {
        readyLoanId = testData.getEhubLoanId();
    }

    private Checkout whenCheckout() throws EhubException {
        return underTest.checkout(authInfo, fields, LANGUAGE);
    }

    private CheckoutMetadata whenFindCheckoutMetadataByLmsLoandId() throws EhubException {
        return underTest.findCheckoutByLmsLoanId(authInfo, lmsLoanId, LANGUAGE);
    }

    private Checkout whenGetCheckoutByLoanId() throws EhubException {
        return underTest.getCheckout(authInfo, readyLoanId, LANGUAGE);
    }
}
