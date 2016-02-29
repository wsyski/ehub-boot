/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Skeletal implementation of an {@link IContentProviderDataAccessor}. It should be sub-classed to provide the data
 * accessor for the specific Content Provider.
 */
public abstract class AbstractContentProviderDataAccessor implements IContentProviderDataAccessor {
    @Autowired
    private IContentFactory contentFactory;

    protected ContentLinks createContent(final List<String> contentUrls, final FormatDecoration formatDecoration) {
        return contentFactory.create(contentUrls, formatDecoration);
    }

    protected ContentProviderLoanMetadata.Builder newContentProviderLoanMetadataBuilder(final CommandData data, final Date expirationDate) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(contentProviderFormatId);
        return new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId, formatDecoration);
    }
}
