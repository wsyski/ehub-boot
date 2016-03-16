/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.checkout.Content;
import org.apache.commons.lang3.Validate;

import java.util.Date;

/**
 * Represents a loan at a Content Provider.
 */
public class ContentProviderLoan {
    private final ContentProviderLoanMetadata metadata;
    private Content content;

    public ContentProviderLoan(final ContentProviderLoanMetadata metadata, final Content content) {
        Validate.notNull(metadata, "The ContentProviderLoanMetadata can't be null");
        this.metadata = metadata;
        this.content = content;
    }

    public String id() {
        return metadata.getId();
    }

    public Date expirationDate() {
        return metadata.getExpirationDate();
    }

    public Content content() {
        return content;
    }

    public ContentProviderLoanMetadata getMetadata() {
        return metadata;
    }

}
