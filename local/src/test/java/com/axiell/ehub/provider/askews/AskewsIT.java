package com.axiell.ehub.provider.askews;

import static org.mockito.BDDMockito.given;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderIT;

public class AskewsIT extends AbstractContentProviderIT {
    private static final Integer AUTH_ID = 0;
    private static final String TOKEN_KEY = "g94ngpts3ngmkeaqtz953dbmutyndw";
    private static final String BARCODE = "axiell";
    private static final Integer LOAN_DURATION = 1;
    private static final String RECORD_ID = "9781843176343-6";

    private IAskewsFacade underTest;

    private UserLookupResult userLookupResult;
    private LoanRequestResult loanRequestResult;
    private Integer loanId;
    private ArrayOfLoanDetails arrayOfLoanDetails;

    @Before
    public void setUpElibFacade() {
        underTest = new AskewsFacade();
    }

    /*
     * Test is temporarily ignored. 
     */
    @Test
    @Ignore
    public void createLoan() {
        givenAskewsProperties();
        whenGetUserId();
        whenProcessLoan();
        thenLoanRequestResultContainsExpectedElements();
        whenGetLoanDetails();
        thenLoanDetailsResultContainsExpectedElements();
    }

    private void givenAskewsProperties() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_AUTHID)).willReturn(
                AUTH_ID.toString());
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_TOKENKEY)).willReturn(
                TOKEN_KEY.toString());
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_BARCODE)).willReturn(
                BARCODE.toString());
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_LOAN_DURATION))
                .willReturn(LOAN_DURATION.toString());
    }

    private void whenGetUserId() {
        userLookupResult = underTest.getUserID(BARCODE, AUTH_ID, TOKEN_KEY);
    }

    private void whenProcessLoan() {
        loanRequestResult = underTest.processLoan(userLookupResult.getUserid(), AUTH_ID, RECORD_ID, LOAN_DURATION,
                TOKEN_KEY);
    }

    private void thenLoanRequestResultContainsExpectedElements() {
        Assert.assertNotNull(loanRequestResult);
        loanId = loanRequestResult.getLoanid();
        Assert.assertNotNull(loanId);
        Assert.assertEquals(Integer.valueOf(1), loanRequestResult.getLoanRequestSuccess());
    }
    
    private void whenGetLoanDetails() {
        arrayOfLoanDetails = underTest.getLoanDetails(userLookupResult.getUserid(), AUTH_ID, null, loanId, TOKEN_KEY);
    }
    
    private void thenLoanDetailsResultContainsExpectedElements() {
        Assert.assertNotNull(arrayOfLoanDetails);
        Assert.assertNotNull(arrayOfLoanDetails.getLoanDetails());
        Assert.assertTrue(arrayOfLoanDetails.getLoanDetails().size() == 1);
        LoanDetails loanDetails = arrayOfLoanDetails.getLoanDetails().get(0);
        Assert.assertNotNull(loanDetails);
        Assert.assertFalse(loanDetails.isHasFailed());
        Assert.assertNotNull(loanDetails.getDownloadURL());
        Assert.assertNotNull(loanDetails.getDownloadURL().getValue());
        Assert.assertTrue(loanDetails.getDownloadURL().getValue().startsWith("http"));
    }
}
