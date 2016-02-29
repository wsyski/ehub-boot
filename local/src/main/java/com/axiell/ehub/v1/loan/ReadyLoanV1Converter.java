package com.axiell.ehub.v1.loan;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.provider.record.format.Format;

import java.util.List;

import static com.axiell.ehub.provider.record.format.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.provider.record.format.ContentDisposition.STREAMING;

class ReadyLoanV1Converter {

    private ReadyLoanV1Converter() {
    }

    static ReadyLoan_v1 convert(Checkout checkout) {
        CheckoutMetadata checkoutMetadata = checkout.metadata();
        LmsLoan_v1 lmsLoan_v1 = new LmsLoan_v1(checkoutMetadata.lmsLoanId());
        IContent_v1 content_v1 = convertToContent(checkout);
        ContentProviderLoanMetadata_v1 contentToContentProviderLoanMetadata = convert(checkoutMetadata);
        ContentProviderLoan_v1 contentProviderLoan_v1 = new ContentProviderLoan_v1(contentToContentProviderLoanMetadata, content_v1);
        return new ReadyLoan_v1(checkoutMetadata.id(), lmsLoan_v1, contentProviderLoan_v1);
    }

    private static ContentProviderLoanMetadata_v1 convert(CheckoutMetadata checkoutMetadata) {
        ContentProviderLoanMetadata_v1 contentProviderLoanMetadata_v1 = new ContentProviderLoanMetadata_v1();
        contentProviderLoanMetadata_v1.setId(checkoutMetadata.contentProviderLoanId());
        contentProviderLoanMetadata_v1.setExpirationDate(checkoutMetadata.expirationDate());
        return contentProviderLoanMetadata_v1;
    }

    private static IContent_v1 convertToContent(Checkout checkout) {
        CheckoutMetadata checkoutMetadata = checkout.metadata();
        Format format = checkoutMetadata.format();
        List<ContentLink> contentLinks = checkout.contentLinks().getContentLinks();
        String href = contentLinks.get(0).href();
        IContent_v1 content_v1 = null;

        if (DOWNLOADABLE.equals(format.contentDisposition())) {
            content_v1 = new DownloadableContent_v1(href);
        } else if (STREAMING.equals(format.contentDisposition())) {
            content_v1 = new StreamingContent_v1(href, format.playerWidth(), format.playerHeight());
        }
        return content_v1;
    }
}
