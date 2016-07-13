package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static com.axiell.ehub.provider.record.format.FormatBuilder.streamingFormat;
import static com.axiell.ehub.provider.record.format.FormatDTOMatcher.matchesExpectedFormatDTO;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutMetadataFactoryTest {
    private static final long EHUB_LOAN_ID = 2L;
    private static final Date EXP_DATE = new Date();
    private static final String CP_LOAN_ID = "cpLoanId";
    private static final String LMS_LOAN_ID = "lmsLoanId";
    private static final String LANGUAGE = "en";
    private CheckoutMetadataFactory underTest;
    @Mock
    private EhubLoan ehubLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private FormatDecoration formatDecoration;
    private Format format;
    @Mock
    private FormatDTO formatDTO;
    @Mock
    private LmsLoan lmsLoan;
    @Mock
    private IFormatFactory formatFactory;
    private CheckoutMetadata actualCheckoutMetadata;

    @Before
    public void setUpUnderTest() {
        underTest = new CheckoutMetadataFactory();
        format = streamingFormat();
        given(formatFactory.create(formatDecoration, LANGUAGE)).willReturn(format);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
    }

    @Test
    public void createFromEhubLoan() throws Exception {
        given(contentProviderLoanMetadata.getFirstFormatDecoration()).willReturn(formatDecoration);
        given(contentProviderLoanMetadata.getExpirationDate()).willReturn(EXP_DATE);
        given(contentProviderLoanMetadata.getId()).willReturn(CP_LOAN_ID);
        given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(lmsLoan.getId()).willReturn(LMS_LOAN_ID);
        given(ehubLoan.getLmsLoan()).willReturn(lmsLoan);
        given(ehubLoan.getId()).willReturn(EHUB_LOAN_ID);
        whenCreate();
        thenActualEhubLoanIdEqualsExpected();
        thenActualExpirationDateEqualsExpected();
        thenActualContentProviderLoanEqualsExpected();
        thenLmsLoanIdEqualsExpected();
        thenActualFormatEqualsExpected();
    }

    private void whenCreate() {
        actualCheckoutMetadata = underTest.create(ehubLoan, formatDecoration, LANGUAGE, false);
    }

    private void thenActualEhubLoanIdEqualsExpected() {
        assertEquals(EHUB_LOAN_ID, actualCheckoutMetadata.getId());
    }

    private void thenActualExpirationDateEqualsExpected() {
        assertEquals(EXP_DATE, actualCheckoutMetadata.getExpirationDate());
    }

    private void thenActualContentProviderLoanEqualsExpected() {
        assertEquals(CP_LOAN_ID, actualCheckoutMetadata.getContentProviderLoanId());
    }

    private void thenLmsLoanIdEqualsExpected() {
        assertEquals(LMS_LOAN_ID, actualCheckoutMetadata.getLmsLoanId());
    }

    private void thenActualFormatEqualsExpected() {
        assertThat(actualCheckoutMetadata.getFormat().toDTO(), matchesExpectedFormatDTO(format.toDTO()));
    }
}
