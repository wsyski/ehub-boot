/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Skeletal implementation of an {@link IContentProviderDataAccessor}. It should be sub-classed to provide the data
 * accessor for the specific Content Provider.
 */
public abstract class AbstractContentProviderDataAccessor implements IContentProviderDataAccessor {

    protected ContentLinks createContentLinks(final List<String> contentLinkHrefs) {
        return new ContentLinks(contentLinkHrefs.stream().map(ContentLink::new).collect(Collectors.toList()));
    }

    protected ContentLinks createContentLinks(final String contentLinkHref) {
        return new ContentLinks(Collections.singletonList(new ContentLink(contentLinkHref)));
    }

    protected ContentProviderLoanMetadata.Builder newContentProviderLoanMetadataBuilder(final CommandData data, final Date expirationDate) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderIssueId = data.getContentProviderIssueId();
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(contentProviderFormatId);
        return new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId, formatDecoration).issueId(contentProviderIssueId);
    }
}
