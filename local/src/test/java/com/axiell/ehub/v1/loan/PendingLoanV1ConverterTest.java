package com.axiell.ehub.v1.loan;

import com.axiell.ehub.loan.PendingLoan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PendingLoanV1ConverterTest {
    private static final String CONTENT_PROVIDER_NAME = "contentProviderName";
    private static final String FORMAT_ID = "formatId";
    public static final String CONTENT_PROVIDER_RECORD_ID = "contentProviderRecordId";
    public static final String LMS_RECORD_ID = "lmsRecordId";
    @Mock
    private PendingLoan_v1 pendingLoan_v1;
    private PendingLoan actualPendingLoan;

    @Test
    public void convert() {
        given(pendingLoan_v1.getContentProviderFormatId()).willReturn(FORMAT_ID);
        given(pendingLoan_v1.getContentProviderName()).willReturn(CONTENT_PROVIDER_NAME);
        given(pendingLoan_v1.getContentProviderRecordId()).willReturn(CONTENT_PROVIDER_RECORD_ID);
        given(pendingLoan_v1.getLmsRecordId()).willReturn(LMS_RECORD_ID);
        whenConvert();
        thenContentProviderFormatIdEqualsExpected();
        thenContentProviderNametEqualsExpected();
        thenContentProviderRecordIdEqualsExpected();
        thenActualLmsRecordIdEqualsExpected();
    }

    private void whenConvert() {
        actualPendingLoan = PendingLoanV1Converter.convert(pendingLoan_v1);
    }

    private void thenContentProviderFormatIdEqualsExpected() {
        assertEquals(FORMAT_ID, actualPendingLoan.getContentProviderFormatId());
    }

    private void thenContentProviderNametEqualsExpected() {
        assertEquals(CONTENT_PROVIDER_NAME, actualPendingLoan.getContentProviderName());
    }

    private void thenContentProviderRecordIdEqualsExpected() {
        assertEquals(CONTENT_PROVIDER_RECORD_ID, actualPendingLoan.getContentProviderRecordId());
    }

    private void thenActualLmsRecordIdEqualsExpected() {
        assertEquals(LMS_RECORD_ID, actualPendingLoan.getLmsRecordId());
    }
}
