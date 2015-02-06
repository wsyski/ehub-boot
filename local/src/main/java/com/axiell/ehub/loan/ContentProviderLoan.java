/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import java.util.Date;

import com.axiell.ehub.checkout.ContentLink;
import org.apache.commons.lang3.Validate;

/**
 * Represents a loan at a Content Provider.
 */
public class ContentProviderLoan {
    private final ContentProviderLoanMetadata metadata;
    private ContentLink contentLink;

    public ContentProviderLoan(ContentProviderLoanMetadata metadata, ContentLink contentLink) {
        Validate.notNull(metadata, "The ContentProviderLoanMetadata can't be null");
        this.metadata = metadata;
        this.contentLink = contentLink;
    }

    public String id() {
        return metadata.getId();
    }

    public Date expirationDate() {
        return metadata.getExpirationDate();
    }

    public ContentLink contentLink() {
        return contentLink;
    }

    public ContentProviderLoanMetadata getMetadata() {
        return metadata;
    }

}
