package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public class CommandData implements ICommandData {
    private final ContentProviderConsumer contentProviderConsumer;
    private final String libraryCard;
    private String pin;
    private String language;
    private String contentProviderRecordId;
    private String contentProviderFormatId;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    private FormatDecoration formatDecoration;
    private String contentUrl;
    private IContent content;

    protected CommandData(final ContentProviderConsumer contentProviderConsumer, final String libraryCard) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.libraryCard = libraryCard;
    }

    public static CommandData newInstance(final ContentProviderConsumer contentProviderConsumer, final String libraryCard) {
        return new CommandData(contentProviderConsumer, libraryCard);
    }

    public ContentProviderConsumer getContentProviderConsumer() {
        return contentProviderConsumer;
    }

    public String getLibraryCard() {
        return libraryCard;
    }

    public String getPin() {
        return pin;
    }

    public CommandData setPin(String pin) {
        this.pin = pin;
        return this;
    }

    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    public String getLanguage() {
        return language;
    }

    public CommandData setLanguage(String language) {
        this.language = language;
        return this;
    }

    public CommandData setContentProviderRecordId(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
        return this;
    }

    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }

    public CommandData setPendingLoan(final PendingLoan pendingLoan) {
        this.contentProviderRecordId = pendingLoan.getContentProviderRecordId();
        this.contentProviderFormatId = pendingLoan.getContentProviderFormatId();
        return this;
    }

    public ContentProviderLoanMetadata getContentProviderLoanMetadata() {
        return contentProviderLoanMetadata;
    }

    public CommandData setContentProviderLoanMetadata(ContentProviderLoanMetadata contentProviderLoanMetadata) {
        this.contentProviderLoanMetadata = contentProviderLoanMetadata;
        return this;
    }

    public FormatDecoration getFormatDecoration() {
        return formatDecoration;
    }

    public CommandData setFormatDecoration(FormatDecoration formatDecoration) {
        this.formatDecoration = formatDecoration;
        return this;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public CommandData setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public IContent getContent() {
        return content;
    }

    public CommandData setContent(IContent content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "CommandData{" +
                "contentProviderConsumer=" + contentProviderConsumer +
                ", libraryCard=" + libraryCard +
                ", pin=" + pin +
                ", language=" + language +
                ", contentProviderRecordId=" + contentProviderRecordId +
                ", contentProviderFormatId=" + contentProviderFormatId +
                ", contentProviderLoanMetadata=" + contentProviderLoanMetadata +
                ", formatDecoration=" + formatDecoration +
                ", contentUrl=" + contentUrl +
                ", content=" + content +
                "}";
    }
}
