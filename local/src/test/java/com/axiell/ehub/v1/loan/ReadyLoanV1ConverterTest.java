package com.axiell.ehub.v1.loan;

import com.axiell.ehub.loan.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ReadyLoanV1ConverterTest {
    private static final Long LOAN_ID = 1L;
    private static final Date EXPIRATION_DATE = new Date();
    private static final String URL = "url";
    private static final int HEIGHT = 1;
    private static final int WIDTH = 2;
    @Mock
    private ReadyLoan readyLoan;
    @Mock
    private LmsLoan lmsLoan;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private StreamingContent streamingContent;
    @Mock
    private DownloadableContent downloadableContent;
    private ReadyLoan_v1 actualReadyLoan_v1;
    private ContentProviderLoan_v1 actualContentProviderLoan_v1;

    @Test
    public void convert_streamingContent() {
        givenStreamingContent();
        given(contentProviderLoanMetadata.getId()).willReturn(LOAN_ID.toString());
        given(contentProviderLoanMetadata.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(contentProviderLoan.getMetadata()).willReturn(contentProviderLoanMetadata);
        given(readyLoan.getContentProviderLoan()).willReturn(contentProviderLoan);
        given(lmsLoan.getId()).willReturn(LOAN_ID.toString());
        given(readyLoan.getLmsLoan()).willReturn(lmsLoan);
        given(readyLoan.getId()).willReturn(LOAN_ID);
        whenConvert();
        thenActualReadyLoanIdEqualsExpected();
        thenActualContentProviderLoanEqualsExpected();
        thenActualExpirationDateEqualsExpectedExpirationDate();
        thenActualStreamingContentEqualsExpected();
        thenActualLmsLoanEqualsExpected();
    }

    private void givenStreamingContent() {
        given(streamingContent.getUrl()).willReturn(URL);
        given(streamingContent.getHeight()).willReturn(HEIGHT);
        given(streamingContent.getWidth()).willReturn(WIDTH);
        given(contentProviderLoan.getContent()).willReturn(streamingContent);
    }

    private void whenConvert() {
        actualReadyLoan_v1 = ReadyLoanV1Converter.convert(readyLoan);
        actualContentProviderLoan_v1 = actualReadyLoan_v1.getContentProviderLoan();
    }

    private void thenActualReadyLoanIdEqualsExpected() {
        assertEquals(LOAN_ID, actualReadyLoan_v1.getId());
    }

    private void thenActualContentProviderLoanEqualsExpected() {
        assertEquals(LOAN_ID.toString(), actualContentProviderLoan_v1.getId());
    }

    private void thenActualExpirationDateEqualsExpectedExpirationDate() {
        assertEquals(EXPIRATION_DATE, actualContentProviderLoan_v1.getExpirationDate());
    }

    private void thenActualStreamingContentEqualsExpected() {
        StreamingContent_v1 actualContent = (StreamingContent_v1) actualContentProviderLoan_v1.getContent();
        assertEquals(URL, actualContent.getUrl());
        assertEquals(HEIGHT, actualContent.getHeight());
        assertEquals(WIDTH, actualContent.getWidth());
    }

    private void thenActualLmsLoanEqualsExpected() {
        LmsLoan_v1 actualLmsLoan_v1 = actualReadyLoan_v1.getLmsLoan();
        assertEquals(LOAN_ID.toString(), actualLmsLoan_v1.getId());
    }

    @Test
    public void convert_downloadableContent() {
        givenDownloadableContent();
        given(contentProviderLoanMetadata.getId()).willReturn(LOAN_ID.toString());
        given(contentProviderLoanMetadata.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(contentProviderLoan.getMetadata()).willReturn(contentProviderLoanMetadata);
        given(readyLoan.getContentProviderLoan()).willReturn(contentProviderLoan);
        given(lmsLoan.getId()).willReturn(LOAN_ID.toString());
        given(readyLoan.getLmsLoan()).willReturn(lmsLoan);
        given(readyLoan.getId()).willReturn(LOAN_ID);
        whenConvert();
        thenActualReadyLoanIdEqualsExpected();
        thenActualContentProviderLoanEqualsExpected();
        thenActualExpirationDateEqualsExpectedExpirationDate();
        thenActualDownloadableContentEqualsExpected();
        thenActualLmsLoanEqualsExpected();
    }

    private void givenDownloadableContent() {
        given(downloadableContent.getUrl()).willReturn(URL);
        given(contentProviderLoan.getContent()).willReturn(downloadableContent);
    }

    private void thenActualDownloadableContentEqualsExpected() {
        DownloadableContent_v1 actualContent = (DownloadableContent_v1) actualContentProviderLoan_v1.getContent();
        assertEquals(URL, actualContent.getUrl());
    }
}
