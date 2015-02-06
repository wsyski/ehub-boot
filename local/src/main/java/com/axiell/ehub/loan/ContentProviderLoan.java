/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import java.util.Date;

import org.apache.commons.lang3.Validate;

/**
 * Represents a loan at a Content Provider.
 */
public class ContentProviderLoan {
    private final ContentProviderLoanMetadata metadata;
    private IContent content;

    /**
     * Constructs a new {@link ContentProviderLoan}.
     * 
     * @param metadata the metadata of the {@link ContentProviderLoan}
     * @param content the getContent of the {@link ContentProviderLoan}
     */
    public ContentProviderLoan(ContentProviderLoanMetadata metadata, IContent content) {
        Validate.notNull(metadata, "The ContentProviderLoanMetadata can't be null");
        this.metadata = metadata;
        this.content = content;
    }

    public String getId() {
        return metadata.getId();
    }

    public Date getExpirationDate() {
        return metadata.getExpirationDate();
    }

    public IContent getContent() {
        return content;
    }

    public ContentProviderLoanMetadata getMetadata() {
        return metadata;
    }

}
