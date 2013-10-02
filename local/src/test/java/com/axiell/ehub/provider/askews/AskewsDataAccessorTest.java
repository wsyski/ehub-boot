package com.axiell.ehub.provider.askews;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;
import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessorTest;

public class AskewsDataAccessorTest extends AbstractContentProviderDataAccessorTest {
    private static final String RECORD_ID = "1";
    private static final String FORMAT_ID = "formatId";
    private static final Integer LOAN_ID = 1;
    private static final Integer LOAN_REQUEST_SUCCESS = 1;
    private static final Integer LOAN_REQUEST_NOT_SUCCESS = -1;
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
    @Mock
    private JAXBElement<String> errorDesc;

    @Before
    public void setUpAskewsDataAccessor() {
	underTest = new AskewsDataAccessor();
	ReflectionTestUtils.setField(underTest, "askewsFacade", askewsFacade);
	ReflectionTestUtils.setField(underTest, "expirationDateFactory", expirationDateFactory);
    }

    @Test
    public void createLoan() {
	givenPendingLoan();
	givenProcessLoan();
	givenLoanRequestSuccess();
	givenLoanId();
	givenLoanDetails();
	givenLoanHasNotFailed();
	givenDownloadUrlInLoanDetail();
	givenContentProvider();
	givenFormatDecorationFromContentProvider();
	givenContentDisposition();
	givenExpirationDate();
	whenCreateLoan();
	thenActualLoanContainsDownloadUrl();
    }

    private void givenPendingLoan() {
	given(pendingLoan.getContentProviderRecordId()).willReturn(RECORD_ID);
	given(pendingLoan.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    private void givenProcessLoan() {
	given(askewsFacade.processLoan(contentProviderConsumer, RECORD_ID)).willReturn(loanRequestResult);
    }

    private void givenLoanRequestSuccess() {
	given(loanRequestResult.getLoanRequestSuccess()).willReturn(LOAN_REQUEST_SUCCESS);
    }

    private void givenLoanId() {
	given(loanRequestResult.getLoanid()).willReturn(LOAN_ID);
    }
    
    private void givenLoanDetails() {
	List<LoanDetails> loanDetailsList = new ArrayList<LoanDetails>();
	loanDetailsList.add(loanDetail);
	given(arrayOfLoanDetails.getLoanDetails()).willReturn(loanDetailsList);
	given(askewsFacade.getLoanDetails(contentProviderConsumer, LOAN_ID.toString())).willReturn(arrayOfLoanDetails);
    }

    private void givenLoanHasNotFailed() {
	given(loanDetail.isHasFailed()).willReturn(false);
    }

    private void givenDownloadUrlInLoanDetail() {
	given(loanDetail.getDownloadURL()).willReturn(new JAXBElement<String>(new QName(""), String.class, DOWNLOAD_URL));
    }

    private void whenCreateLoan() {
	actualLoan = underTest.createLoan(contentProviderConsumer, CARD, PIN, pendingLoan);
    }

    @Test
    public void createLoanWhenStatusIsNotSuccess() {
	givenPendingLoan();
	givenProcessLoan();
	givenLoanRequestIsNotSucess();
	givenLoanRequestResultErrorDesc();
	try {
	    whenCreateLoan();
	    Assert.fail("Should throw an InternalServerErrorException");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void givenLoanRequestIsNotSucess() {
	given(loanRequestResult.getLoanRequestSuccess()).willReturn(LOAN_REQUEST_NOT_SUCCESS);
    }

    private void givenLoanRequestResultErrorDesc() {
	given(loanRequestResult.getErrorDesc()).willReturn(errorDesc);
    }

    @Test
    public void createLoanHasFailed() {
	givenPendingLoan();
	givenProcessLoan();
	givenLoanRequestSuccess();
	givenLoanId();
	givenLoanDetails();
	givenLoanHasFailed();
	givenLoanDetailErrorDesc();
	try {
	    whenCreateLoan();
	    Assert.fail("Should throw an InternalServerErrorException");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void givenLoanHasFailed() {
	given(loanDetail.isHasFailed()).willReturn(true);
    }

    private void givenLoanDetailErrorDesc() {
	given(loanDetail.getErrorDesc()).willReturn(errorDesc);
    }
    
    @Test 
    public void createLoanWasNotSuccessful() {
	givenPendingLoan();
	givenProcessLoan();
	givenLoanRequestIsNotSucess();
	givenLoanRequestResultErrorDesc();
	try {
	    whenCreateLoan();
	    Assert.fail("Should throw an InternalServerErrorException");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    @Test
    public void getContent() {
	givenContentProviderLoanId();
	givenContentProvider();
	givenFormatDecorationFromContentProviderLoanMetadata();
	givenContentDisposition();
	givenLoanDetails();
	givenLoanStatusIsTitleHasBeenProcessed();
	givenDownloadUrlInLoanDetail();
	whenGetContent();
	thenActualContentContainsDownloadUrl();
    }

    private void givenContentProviderLoanId() {
	given(loanMetadata.getId()).willReturn(LOAN_ID.toString());
    }

    private void givenLoanStatusIsTitleHasBeenProcessed() {
	given(loanDetail.getLoanStatus()).willReturn(TITLE_HAS_BEEN_PROCESSED);
    }
    
    private void whenGetContent() {
	actualContent = underTest.getContent(contentProviderConsumer, CARD, PIN, loanMetadata);
    }
    
    @Test
    public void getFormatsWhenNoFormatDecoration() {
	givenContentProvider();
	try{
	    whenGetFormats();    
	    Assert.fail("An InternalServerErrorException should have been thrown");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}	
    }

    private void whenGetFormats() {
	actualFormats = underTest.getFormats(contentProviderConsumer, RECORD_ID, LANGUAGE);
    }
    
    @Test
    public void getFormatsWhenNoTextBundle() {
	givenContentProvider();
	givenFormatDecorationFromContentProvider();
	try{
	    whenGetFormats();    
	    Assert.fail("An InternalServerErrorException should have been thrown");
	} catch (InternalServerErrorException e) {
	    EhubAssert.thenInternalServerErrorExceptionIsThrown(e);
	}
    }
    
    @Test
    public void getFormats() {
	givenContentProvider();
	givenFormatDecorationFromContentProvider();
	givenTextBundle();
	givenEhubFormatNameAndDescription();
	whenGetFormats();
	thenFormatHasEhubFormatNameAndDescription();
    }
}
