package com.axiell.ehub.provider.askews;

import com.askews.api.ArrayOfLoanDetails;
import com.askews.api.LoanDetails;
import com.askews.api.LoanRequestResult;
import com.askews.api.UserLookupResult;
import com.axiell.ehub.EhubAssert;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

public class AskewsDataAccessorTest extends ContentProviderDataAccessorTestFixture {
    private static final String RECORD_ID = "1";
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
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "askewsFacade", askewsFacade);
        ReflectionTestUtils.setField(underTest, "expirationDateFactory", expirationDateFactory);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
    }

    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLibraryCardInPatron();
        givenLanguageInCommandData();
        givenProcessLoan();
        givenLoanRequestSuccess();
        givenLoanId();
        givenLoanDetails();
        givenLoanHasNotFailed();
        givenDownloadUrlInLoanDetail();
        givenFormatDecorationFromContentProvider();
        givenDownloadableContentDisposition();
        givenContentLink();
        givenExpirationDate();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
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
        given(loanDetail.getDownloadURL()).willReturn(new JAXBElement<String>(new QName(""), String.class, CONTENT_HREF));
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    @Test
    public void createLoanWhenStatusIsNotSuccess() {
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
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
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLibraryCardInPatron();
        givenLanguageInCommandData();
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
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderFormatIdInCommandData();
        givenLibraryCardInPatron();
        givenLanguageInCommandData();
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
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanId();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationFromContentProviderLoanMetadata();
        givenDownloadableContentDisposition();
        givenLoanDetails();
        givenLoanStatusIsTitleHasBeenProcessed();
        givenDownloadUrlInLoanDetail();
        givenContentLink();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    private void givenContentProviderLoanId() {
        given(loanMetadata.getId()).willReturn(LOAN_ID.toString());
    }

    private void givenLoanStatusIsTitleHasBeenProcessed() {
        given(loanDetail.getLoanStatus()).willReturn(TITLE_HAS_BEEN_PROCESSED);
    }

    private void whenGetContent() {
        actualContentLink = underTest.getContent(commandData);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    @Test
    public void getFormats() {
        givenFormatFromFormatFactory();
        givenContentProviderConsumerInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenLanguageInCommandData();
        givenFormatDecorationFromContentProvider();
        givenTextBundle();
        whenGetFormats();
        thenActualFormatEqualsExpected();
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ASKEWS;
    }
}
