package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;

public class CommandData implements ICommandData {
    private final ContentProviderConsumer contentProviderConsumer;
    private final Patron patron;
    private final String language;
    private String contentProviderRecordId;
    private String contentProviderFormatId;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    private String contentUrl;
    private ContentLink contentLink;

    protected CommandData(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
        this.language = language;
    }

    public static CommandData newInstance(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        return new CommandData(contentProviderConsumer, patron, language);
    }

    public ContentProviderConsumer getContentProviderConsumer() {
        return contentProviderConsumer;
    }

    public Patron getPatron() {
        return patron;
    }

    public String getLanguage() {
        return language;
    }

    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    public CommandData setContentProviderRecordId(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
        return this;
    }

    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }

    public CommandData setPendingLoan(final PendingLoan pendingLoan) {
        this.contentProviderRecordId = pendingLoan.contentProviderRecordId();
        this.contentProviderFormatId = pendingLoan.contentProviderFormatId();
        return this;
    }

    public ContentProviderLoanMetadata getContentProviderLoanMetadata() {
        return contentProviderLoanMetadata;
    }

    public CommandData setContentProviderLoanMetadata(ContentProviderLoanMetadata contentProviderLoanMetadata) {
        this.contentProviderLoanMetadata = contentProviderLoanMetadata;
        return this;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public CommandData setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public ContentLink getContent() {
        return contentLink;
    }

    public CommandData setContent(ContentLink contentLink) {
        this.contentLink = contentLink;
        return this;
    }

    @Override
    public String toString() {
        return "CommandData{" +
                "contentProviderConsumer=" + contentProviderConsumer +
                ", patron=" + patron +
                ", language=" + language +
                ", contentProviderRecordId=" + contentProviderRecordId +
                ", contentProviderFormatId=" + contentProviderFormatId +
                ", contentProviderLoanMetadata=" + contentProviderLoanMetadata +
                ", contentUrl=" + contentUrl +
                ", contentLink=" + contentLink +
                "}";
    }
}
