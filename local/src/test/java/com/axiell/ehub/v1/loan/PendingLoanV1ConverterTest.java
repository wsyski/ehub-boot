package com.axiell.ehub.v1.loan;

import com.axiell.ehub.Fields;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.axiell.ehub.FieldsBuilder.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PendingLoanV1ConverterTest {
    @Mock
    private PendingLoan_v1 pendingLoan_v1;
    private Fields actualFields;

    @Test
    public void convert() {
        given(pendingLoan_v1.getContentProviderFormatId()).willReturn(CONTENT_PROVIDER_FORMAT_ID);
        given(pendingLoan_v1.getContentProviderName()).willReturn(CONTENT_PROVIDER_ALIAS);
        given(pendingLoan_v1.getContentProviderRecordId()).willReturn(CONTENT_PROVIDER_RECORD_ID);
        given(pendingLoan_v1.getLmsRecordId()).willReturn(LMS_RECORD_ID);
        whenConvert();
        thenContentProviderFormatIdEqualsExpected();
        thenContentProviderNametEqualsExpected();
        thenContentProviderRecordIdEqualsExpected();
        thenActualLmsRecordIdEqualsExpected();
    }

    private void whenConvert() {
        actualFields = PendingLoanConverter_v1.convert(pendingLoan_v1);
    }

    private void thenContentProviderFormatIdEqualsExpected() {
        assertEquals(CONTENT_PROVIDER_FORMAT_ID, actualFields.getRequiredValue("contentProviderFormatId"));
    }

    private void thenContentProviderNametEqualsExpected() {
        assertEquals(CONTENT_PROVIDER_ALIAS, actualFields.getRequiredValue("contentProviderAlias"));
    }

    private void thenContentProviderRecordIdEqualsExpected() {
        assertEquals(CONTENT_PROVIDER_RECORD_ID, actualFields.getRequiredValue("contentProviderRecordId"));
    }

    private void thenActualLmsRecordIdEqualsExpected() {
        assertEquals(LMS_RECORD_ID, actualFields.getRequiredValue("lmsRecordId"));
    }
}
