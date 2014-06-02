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
     * @param libraryCard             the library card of the end-user who wants to borrow the media, can be <code>null</code>
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider}
     * @param language                the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link Formats} instance
     */
    Formats getFormats(ContentProviderConsumer contentProviderConsumer, String libraryCard, String contentProviderRecordId, String language);

    /**
     * Creates a loan at the {@link ContentProvider}.
     *
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be used
     * @param libraryCard             the library card of the end-user who wants to borrow the media
     * @param pin                     the pin of the library card
     * @param pendingLoan
     * @param language                the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link ContentProviderLoan}
     */
    ContentProviderLoan createLoan(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin, PendingLoan pendingLoan, String language);

    /**
     * Gets the content of an existing {@link ContentProviderLoan}.
     *
     * @param contentProviderConsumer     the {@link ContentProviderConsumer} to be used
     * @param libraryCard                 the library card of the end-user who has to borrowed the media
     * @param pin                         the pin of the library card
     * @param contentProviderLoanMetadata the metadata of an existing {@link ContentProviderLoan}
     * @param language                    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return an {@link IContent}
     */
    IContent getContent(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin, ContentProviderLoanMetadata contentProviderLoanMetadata, String language);
}
