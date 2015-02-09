/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.Fields;

public class PendingLoan {
    private final Fields fields;

    public PendingLoan(final Fields fields) {
        this.fields = fields;
    }

    public String lmsRecordId() {
        return fields.getRequiredValue("lmsRecordId");
    }

    public String contentProviderAlias() {
        return fields.getRequiredValue("contentProviderAlias");
    }

    public String contentProviderRecordId() {
        return fields.getRequiredValue("contentProviderRecordId");
    }

    public String contentProviderFormatId() {
        return fields.getRequiredValue("contentProviderFormatId");
    }
}
