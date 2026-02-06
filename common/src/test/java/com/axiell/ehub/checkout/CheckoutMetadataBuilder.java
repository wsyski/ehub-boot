package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.FormatBuilder;

import java.util.Date;

public class CheckoutMetadataBuilder {
    public static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    public static final Long CHECKOUT_ID = 3L;
    public static final String LMS_LOAN_ID = "lmsLoanId";
    public static final String ISSUE_ID = "issueId";
    public static final String ISSUE_TITLE = "issueTitle";
    public static final Date EXPIRATION_DATE = new Date();

    public static CheckoutMetadata checkoutMetadataWithDownloadableFormat() {
        return new CheckoutMetadata(CHECKOUT_ID, LMS_LOAN_ID, EXPIRATION_DATE, false, FormatBuilder.downloadableFormat())
                .contentProviderLoanId(CONTENT_PROVIDER_LOAN_ID);
    }

    public static CheckoutMetadata checkoutMetadataWithStreamingFormat() {
        return new CheckoutMetadata(CHECKOUT_ID, LMS_LOAN_ID, EXPIRATION_DATE, false, FormatBuilder.streamingFormat())
                .contentProviderLoanId(CONTENT_PROVIDER_LOAN_ID);
    }

    public static CheckoutMetadata checkoutMetadataWithIssue() {
        return new CheckoutMetadata(CHECKOUT_ID, LMS_LOAN_ID, EXPIRATION_DATE, false, FormatBuilder.streamingFormat()).issueId(ISSUE_ID).issueTitle(ISSUE_TITLE)
                .contentProviderLoanId(CONTENT_PROVIDER_LOAN_ID);
    }
}
