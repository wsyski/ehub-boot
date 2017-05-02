package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.AbstractContentProviderIT;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBElement;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/com/axiell/ehub/provider/askews/askews-context.xml"})
public class AskewsIT extends AbstractContentProviderIT {
    private static final Integer AUTH_ID = 17;
    private static final String TOKEN_KEY = "k60pb4rtmdplhtrnsdjrssw749bxmz";

    private static final String CARD = "axiell";
    private static final String PIN = "1146";
    private static final String LOAN_DURATION = "1";
    private static final String RECORD_ID = "9780007536429-6";
    private static final String LOAN_ID = "1411780";

    @Autowired
    private IAskewsFacade underTest;

    private LoanRequestResult loanRequestResult;
    private ArrayOfLoanDetails arrayOfLoanDetails;

    @Mock
    protected Patron patron;

    @Before
    public void setUpAskewsFacade() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processLoan() {
        givenAskewsProperties();
        givenPatron();
        givenDuration();
        whenProcessLoan();
        thenLoanRequestResultContainsExpectedElements();
    }

    @Test
    public void getLoanDetails() {
        givenAskewsProperties();
        givenPatron();
        whenGetLoanDetails();
        thenLoanDetailsResultContainsExpectedElements();
    }

    private void givenPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
    }

    private void givenAskewsProperties() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_AUTH_ID)).willReturn(AUTH_ID.toString());
        given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.ASKEWS_TOKEN_KEY)).willReturn(TOKEN_KEY);
    }

    private void givenDuration() {
        givenContentProvider();
        given(contentProvider.getProperty(ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS)).willReturn(LOAN_DURATION);
    }

    private void whenProcessLoan() {
        loanRequestResult = underTest.processLoan(contentProviderConsumer, RECORD_ID, patron);
    }

    private void thenLoanRequestResultContainsExpectedElements() {
        Assert.assertNotNull(loanRequestResult);
        Integer loanId = loanRequestResult.getLoanid();
        Assert.assertNotNull(loanId);
        Integer loanRequestSuccess = loanRequestResult.getLoanRequestSuccess();
        Assert.assertNotNull(loanRequestSuccess);
    }

    private void whenGetLoanDetails() {
        arrayOfLoanDetails = underTest.getLoanDetails(contentProviderConsumer, LOAN_ID, patron);
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
