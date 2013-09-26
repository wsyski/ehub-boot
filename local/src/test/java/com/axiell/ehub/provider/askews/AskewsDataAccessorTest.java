package com.axiell.ehub.provider.askews;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;

public class AskewsDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "formatId";
    private static final Integer LOAN_ID = 1;
    private static final Integer USER_ID = 1;
    private static final Integer AUTH_ID = 1;
    private static final Integer LOAN_DURATION = 10;
    private static final String TOKEN_KEY = "tokenKey";
    private static final String BARCODE = "barcode";
    private static final Integer LOAN_REQUEST_SUCCESS = 1;
    private static final Integer TITLE_HAS_BEEN_PROCESSED = 4;
    
    private AskewsDataAccessor underTest;
    @Mock
    private IAskewsFacade askewsFacade;
    @Mock
    private LoanRequestResult loanRequestResult;
    @Mock
    private UserLookupResult userLookupResult;
    @Mock
    private ArrayOfLoanDetails arrayOfLoanDetails;
    @Mock
    private LoanDetails loanDetail;
    
    @Before
    public void setUpPublitDataAccessor() {
        underTest = new AskewsDataAccessor();
        ReflectionTestUtils.setField(underTest, "askewsFacade", askewsFacade);
    }

    @Test
    public void createLoan() {
        givenPendingLoan();
        givenAskewsProperties();
        givenUserId();
        givenProcessLoan();
        givenLoanDetails();
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
        givenContentDisposition();
        whenCreateLoan();
        thenActualLoanContainsDownloadUrl();
    }

    private void givenPendingLoan() {
        given(pendingLoan.getContentProviderRecordId()).willReturn(RECORD_ID);
        given(pendingLoan.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void givenUserId() {
        given(userLookupResult.getUserid()).willReturn(USER_ID);
        given(askewsFacade.getUserID(BARCODE, AUTH_ID, TOKEN_KEY)).willReturn(userLookupResult);
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

    private void givenProcessLoan() {
        given(loanRequestResult.getLoanRequestSuccess()).willReturn(LOAN_REQUEST_SUCCESS);
        given(loanRequestResult.getLoanid()).willReturn(LOAN_ID);
        given(askewsFacade.processLoan(USER_ID, AUTH_ID, RECORD_ID, LOAN_DURATION, TOKEN_KEY)).willReturn(
                loanRequestResult);
    }

    private void givenLoanDetails() {
        List<LoanDetails> loanDetailsList = new ArrayList<LoanDetails>();
        loanDetailsList.add(loanDetail);
        given(loanDetail.getLoanStatus()).willReturn(TITLE_HAS_BEEN_PROCESSED);
        given(loanDetail.getDownloadURL()).willReturn(new JAXBElement<String>(new QName(""), String.class, DOWNLOAD_URL));
        given(arrayOfLoanDetails.getLoanDetails()).willReturn(loanDetailsList);
        given(askewsFacade.getLoanDetails(USER_ID, AUTH_ID, null, LOAN_ID, TOKEN_KEY)).willReturn(
                arrayOfLoanDetails);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(contentProviderConsumer, CARD, PIN, pendingLoan);
    }

    @Test
    public void getContent() {
        givenAskewsProperties();
        givenUserId();
        givenContentProviderLoanId();
        givenContentProvider();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenContentDisposition();
        givenLoanDetails();
        whenGetContent();
        thenActualContentContainsDownloadUrl();
    }

    private void givenContentProviderLoanId() {
        given(loanMetadata.getId()).willReturn(LOAN_ID.toString());
    }
    
    private void whenGetContent() {
        actualContent = underTest.getContent(contentProviderConsumer, CARD, PIN, loanMetadata);
    }
}
