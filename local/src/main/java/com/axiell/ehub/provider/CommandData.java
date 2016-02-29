package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;

import java.util.List;

public class CommandData implements ICommandData {
    private final ContentProviderConsumer contentProviderConsumer;
    private final Patron patron;
    private final String language;
    private String contentProviderRecordId;
    private String contentProviderAlias;
    private String contentProviderFormatId;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    private List<String> contentUrls;
    private ContentLinks contentLinks;

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

    public CommandData setContentProviderRecordId(final String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
        return this;
    }

    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }

    public String getContentProviderAlias() {
        return contentProviderAlias;
    }

    public CommandData setContentProviderAlias(final String contentProviderAlias) {
        this.contentProviderAlias = contentProviderAlias;
        return this;
    }

    public CommandData setPendingLoan(final PendingLoan pendingLoan) {
        this.contentProviderAlias = pendingLoan.contentProviderAlias();
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

    public List<String> getContentUrls() {
        return contentUrls;
    }

    public CommandData setContentUrls(List<String> contentUrl) {
        this.contentUrls = contentUrl;
        return this;
    }

    public ContentLinks getContent() {
        return contentLinks;
    }

    public CommandData setContent(ContentLinks contentLinks) {
        this.contentLinks = contentLinks;
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
                ", contentUrl=" + contentUrls +
                ", contentLinks=" + contentLinks +
                "}";
    }
}
