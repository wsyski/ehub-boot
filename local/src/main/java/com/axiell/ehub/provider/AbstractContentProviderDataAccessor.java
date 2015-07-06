/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Skeletal implementation of an {@link IContentProviderDataAccessor}. It should be sub-classed to provide the data
 * accessor for the specific Content Provider.
 */
public abstract class AbstractContentProviderDataAccessor implements IContentProviderDataAccessor {
    @Autowired
    private IContentFactory contentFactory;

    protected final ContentLink createContent(final String contentUrl, final FormatDecoration formatDecoration) {
        return contentFactory.create(contentUrl, formatDecoration);
    }

    protected final ContentProviderLoanMetadata.Builder newContentProviderLoanMetadataBuilder(final CommandData data, final Date expirationDate) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(contentProviderFormatId);
        return new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId, formatDecoration);
    }
}
