package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.SupplementLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class CommandData implements ICommandData {
    private final ContentProviderConsumer contentProviderConsumer;
    private final Patron patron;
    private final String language;
    private String contentProviderAlias;
    private String contentProviderRecordId;
    private String contentProviderIssueId;
    private String contentProviderFormatId;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    private FormatDecoration formatDecoration;
    private List<String> contentUrls;
    private List<SupplementLink> supplementLinks;
    private Content content;

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

    public String getContentProviderIssueId() {
        return contentProviderIssueId;
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
        this.contentProviderIssueId = pendingLoan.contentProviderIssueId();
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

    public FormatDecoration getFormatDecoration() {
        return formatDecoration;
    }

    public CommandData setFormatDecoration(final FormatDecoration formatDecoration) {
        this.formatDecoration = formatDecoration;
        return this;
    }

    public List<SupplementLink> getSupplementLinks() {
        return supplementLinks;
    }

    public CommandData setSupplementLinks(List<SupplementLink> supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }

    public List<String> getContentUrls() {
        return contentUrls;
    }

    public CommandData setContentUrls(List<String> contentUrl) {
        this.contentUrls = contentUrl;
        return this;
    }

    public Content getContent() {
        return content;
    }

    public CommandData setContent(Content content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
