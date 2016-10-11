package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.stream.Collectors;

public class CommandData implements ICommandData {
    private final ContentProviderConsumer contentProviderConsumer;
    private final Patron patron;
    private final String language;
    private String contentProviderAlias;
    private String contentProviderRecordId;
    private String issueId;
    private String contentProviderFormatId;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    private FormatDecoration formatDecoration;
    private ContentLinks contentLinks;
    private SupplementLinks supplementLinks;
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

    public String getIssueId() {
        return issueId;
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
        this.issueId = pendingLoan.issueId();
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

    public SupplementLinks getSupplementLinks() {
        return supplementLinks;
    }

    public CommandData setSupplementLinks(final SupplementLinks supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }

    public ContentLinks getContentLinks() {
        return contentLinks;
    }

    public CommandData setContentLinks(final ContentLinks contentLinks) {
        this.contentLinks = contentLinks;
        return this;
    }

    public CommandData setContentLinkHrefs(final List<String> contentLinkHrefs) {
        ContentLinks contentLinks = contentLinkHrefs == null ? null : new ContentLinks(contentLinkHrefs.stream().map(ContentLink::new)
                .collect(Collectors.toList()));
        return setContentLinks(contentLinks);
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
