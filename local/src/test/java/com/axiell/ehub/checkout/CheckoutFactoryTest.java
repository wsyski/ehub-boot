package com.axiell.ehub.checkout;

import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutFactoryTest {
    private static final Long EHUB_LOAN_ID = 2L;
    private static final Date EXP_DATE = new Date();
    public static final String CP_LOAN_ID = "cpLoanId";
    public static final String LMS_LOAN_ID = "lmsLoanId";
    @Mock
    private EhubLoan ehubLoan;
//    @Mock
//    private ContentProviderLoan contentProviderLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private LmsLoan lmsLoan;
    private Checkout actualCheckout;

    @Test
    public void createFromEhubLoan() throws Exception {
        given(contentProviderLoanMetadata.getExpirationDate()).willReturn(EXP_DATE);
        given(contentProviderLoanMetadata.getId()).willReturn(CP_LOAN_ID);
//        given(contentProviderLoanMetadata.getFormatDecoration()).willReturn(formatDecoration);
//        given(contentProviderLoan.getMetadata()).willReturn(contentProviderLoanMetadata);
        given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(lmsLoan.getId()).willReturn(LMS_LOAN_ID);
        given(ehubLoan.getLmsLoan()).willReturn(lmsLoan);
        given(ehubLoan.getId()).willReturn(EHUB_LOAN_ID);
    }
}
