package com.axiell.ehub.provider.askews;

import static org.mockito.BDDMockito.given;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

public class AskewsIT extends AbstractContentProviderIT {
    private static final Integer AUTH_ID = 0;
    private static final String TOKEN_KEY = "g94ngpts3ngmkeaqtz953dbmutyndw";
    private static final String BARCODE = "askews2";
    private static final String LOAN_DURATION = "1";
    private static final String RECORD_ID = "9780857892645-6";
    private static final String LOAN_ID = "448562";

    private IAskewsFacade underTest;

    private LoanRequestResult loanRequestResult;
    private ArrayOfLoanDetails arrayOfLoanDetails;

    @Before
    public void setUpAskewsFacade() {
	underTest = new AskewsFacade();
    }

    @Test
    public void processLoan() {
	givenAskewsProperties();
	givenDuration();
	whenProcessLoan();
	thenLoanRequestResultContainsExpectedElements();
    }

    private void givenAskewsProperties() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_AUTH_ID)).willReturn(AUTH_ID.toString());
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_TOKEN_KEY)).willReturn(TOKEN_KEY);
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_BARCODE)).willReturn(BARCODE);
    }

    private void givenDuration() {
	givenContentProvider();
	given(contentProvider.getProperty(ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS)).willReturn(LOAN_DURATION);
    }

    private void whenProcessLoan() {
	loanRequestResult = underTest.processLoan(contentProviderConsumer, RECORD_ID);
    }

    private void thenLoanRequestResultContainsExpectedElements() {
	Assert.assertNotNull(loanRequestResult);
	Integer loanId = loanRequestResult.getLoanid();
	Assert.assertNotNull(loanId);
	Integer loanRequestSuccess = loanRequestResult.getLoanRequestSuccess();
	Assert.assertNotNull(loanRequestSuccess);
    }

    @Test
    public void getLoanDetails() {
	givenAskewsProperties();
	whenGetLoanDetails();
	thenLoanDetailsResultContainsExpectedElements();
    }

    private void whenGetLoanDetails() {
	arrayOfLoanDetails = underTest.getLoanDetails(contentProviderConsumer, LOAN_ID);
    }

    private void thenLoanDetailsResultContainsExpectedElements() {
	Assert.assertNotNull(arrayOfLoanDetails);
	List<LoanDetails> loanDetailsList = arrayOfLoanDetails.getLoanDetails();
	Assert.assertNotNull(loanDetailsList);
	Assert.assertFalse(loanDetailsList.isEmpty());
	LoanDetails loanDetails = loanDetailsList.get(0);
        Assert.assertNotNull(loanDetails);
        Assert.assertFalse(loanDetails.isHasFailed());
        JAXBElement<String> downloadUrl = loanDetails.getDownloadURL();
        Assert.assertNotNull(downloadUrl);
        String contentUrl = downloadUrl.getValue();
        Assert.assertNotNull(contentUrl);
        Assert.assertTrue(contentUrl.startsWith("http"));
    }
}
