package com.axiell.ehub.loan;

import com.axiell.ehub.FieldsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.axiell.ehub.FieldsBuilder.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PendingLoanTest {
    private PendingLoan underTest;

    @Before
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
