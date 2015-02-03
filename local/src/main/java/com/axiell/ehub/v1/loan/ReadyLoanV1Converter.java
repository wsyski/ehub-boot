package com.axiell.ehub.v1.loan;

import com.axiell.ehub.loan.*;

class ReadyLoanV1Converter {

    private ReadyLoanV1Converter() {
    }

    static ReadyLoan_v1 convert(ReadyLoan readyLoan) {
        LmsLoan_v1 lmsLoan_v1 = new LmsLoan_v1(readyLoan.getLmsLoan().getId());
        ContentProviderLoan contentProviderLoan = readyLoan.getContentProviderLoan();
        IContent_v1 content_v1 = convertContent(contentProviderLoan);
        ContentProviderLoanMetadata_v1 contentProviderLoanMetadata_v1 = convertContentProviderLoanMetadata(contentProviderLoan);
        ContentProviderLoan_v1 contentProviderLoan_v1 = new ContentProviderLoan_v1(contentProviderLoanMetadata_v1, content_v1);
        return new ReadyLoan_v1(readyLoan.getId(), lmsLoan_v1, contentProviderLoan_v1);
    }

    private static ContentProviderLoanMetadata_v1 convertContentProviderLoanMetadata(ContentProviderLoan contentProviderLoan) {
        ContentProviderLoanMetadata contentProviderLoanMetadata = contentProviderLoan.getMetadata();
        ContentProviderLoanMetadata_v1 contentProviderLoanMetadata_v1 = new ContentProviderLoanMetadata_v1();
        contentProviderLoanMetadata_v1.setId(contentProviderLoanMetadata.getId());
        contentProviderLoanMetadata_v1.setExpirationDate(contentProviderLoanMetadata.getExpirationDate());
        return contentProviderLoanMetadata_v1;
    }

    private static IContent_v1 convertContent(ContentProviderLoan contentProviderLoan) {
        IContent content = contentProviderLoan.getContent();
        IContent_v1 content_v1 = null;

        if (content instanceof DownloadableContent) {
            DownloadableContent downloadableContent = (DownloadableContent) content;
            content_v1 = new DownloadableContent_v1(downloadableContent.getUrl());
        } else if (content instanceof StreamingContent) {
            StreamingContent streamingContent = (StreamingContent) content;
            content_v1 = new StreamingContent_v1(streamingContent.getUrl(), streamingContent.getWidth(), streamingContent.getHeight());
        }
        return content_v1;
    }
}
