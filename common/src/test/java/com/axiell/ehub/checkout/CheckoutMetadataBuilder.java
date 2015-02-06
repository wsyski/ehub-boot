package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatBuilder;

import java.util.Date;

import static com.axiell.ehub.provider.record.format.FormatBuilder.downloadableFormat;
import static com.axiell.ehub.provider.record.format.FormatBuilder.streamingFormat;

public class CheckoutMetadataBuilder {
    public static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    public static final Date EXPIRATION_DATE = new Date();
    public static final Long ID = 3L;
    public static final String LMS_LOAN_ID = "lmsLoanId";

    public static CheckoutMetadata checkoutMetadataWithDownloadableFormat() {
        Format format = downloadableFormat();
        return new CheckoutMetadata().contentProviderLoanId(CONTENT_PROVIDER_LOAN_ID).expirationDate(EXPIRATION_DATE).format(format).id(ID).lmsLoanId(LMS_LOAN_ID);
    }

    public static CheckoutMetadata checkoutMetadataWithStreamingFormat() {
        Format format = streamingFormat();
        return new CheckoutMetadata().contentProviderLoanId(CONTENT_PROVIDER_LOAN_ID).expirationDate(EXPIRATION_DATE).format(format).id(ID).lmsLoanId(LMS_LOAN_ID);
    }
}
