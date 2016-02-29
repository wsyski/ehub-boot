/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.checkout.ContentLinks;
import org.apache.commons.lang3.Validate;

import java.util.Date;

/**
 * Represents a loan at a Content Provider.
 */
public class ContentProviderLoan {
    private final ContentProviderLoanMetadata metadata;
    private ContentLinks contentLinks;

    public ContentProviderLoan(ContentProviderLoanMetadata metadata, ContentLinks contentLinks) {
        Validate.notNull(metadata, "The ContentProviderLoanMetadata can't be null");
        this.metadata = metadata;
        this.contentLinks = contentLinks;
    }

    public String id() {
        return metadata.getId();
    }

    public Date expirationDate() {
        return metadata.getExpirationDate();
    }

    public ContentLinks contentLinks() {
        return contentLinks;
    }

    public ContentProviderLoanMetadata getMetadata() {
        return metadata;
    }

}
