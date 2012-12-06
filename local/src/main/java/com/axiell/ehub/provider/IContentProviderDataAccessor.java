/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * Defines the data accessor interface for the {@link ContentProvider}s.
 */
public interface IContentProviderDataAccessor {

    /**
     * Gets the available {@link Formats} of the given record.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be used
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider}
     * @param language the ISO 639 alpha-2 or alpha-3 language cod
     * @return a {@link Formats} instance
     */
    Formats getFormats(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language);

    /**
     * Creates a loan at the {@link ContentProvider}.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be used
     * @param libraryCard the library card of the end-user who wants to borrow the media
     * @param pin the pin of the library card
     * @param pendingLoan
     * @return a {@link ContentProviderLoan}
     */
    ContentProviderLoan createLoan(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin, PendingLoan pendingLoan);

    /**
     * Gets the content of an existing {@link ContentProviderLoan}.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be used
     * @param libraryCard the library card of the end-user who has to borrowed the media
     * @param pin the pin of the library card
     * @param contentProviderLoanMetadata the metadata of an existing {@link ContentProviderLoan}
     * @return an {@link IContent}
     */
    IContent getContent(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin, ContentProviderLoanMetadata contentProviderLoanMetadata);

    /**
     * Gets the name of the {@link ContentProvider} for which this {@link IContentProviderDataAccessor} provides an
     * integration.
     * 
     * @return a {@link ContentProviderName}
     */
    ContentProviderName getContentProviderName();
}
