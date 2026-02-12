package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.FieldsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.axiell.ehub.common.FieldsBuilder.CONTENT_PROVIDER_ALIAS;
import static com.axiell.ehub.common.FieldsBuilder.CONTENT_PROVIDER_FORMAT_ID;
import static com.axiell.ehub.common.FieldsBuilder.CONTENT_PROVIDER_RECORD_ID;
import static com.axiell.ehub.common.FieldsBuilder.LMS_RECORD_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PendingLoanTest {
    private PendingLoan underTest;

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new PendingLoan(FieldsBuilder.defaultFields());
    }

    @Test
    public void getLmsRecordId() throws Exception {
        assertThat(underTest.lmsRecordId(), is(LMS_RECORD_ID));
    }

    @Test
    public void getContentProviderName() throws Exception {
        assertThat(underTest.contentProviderAlias(), is(CONTENT_PROVIDER_ALIAS));
    }

    @Test
    public void getContentProviderRecordId() throws Exception {
        assertThat(underTest.contentProviderRecordId(), is(CONTENT_PROVIDER_RECORD_ID));
    }

    @Test
    public void getContentProviderFormatId() throws Exception {
        assertThat(underTest.contentProviderFormatId(), is(CONTENT_PROVIDER_FORMAT_ID));
    }
}
