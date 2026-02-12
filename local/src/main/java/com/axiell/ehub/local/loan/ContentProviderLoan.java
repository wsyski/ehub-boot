package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.checkout.Content;
import org.apache.commons.lang3.Validate;

import java.util.Date;

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
