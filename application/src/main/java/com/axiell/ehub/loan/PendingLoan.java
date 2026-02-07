package com.axiell.ehub.loan;

import com.axiell.ehub.Fields;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class PendingLoan {
    private final Fields fields;

    public PendingLoan(final Fields fields) {
        this.fields = fields;
    }

    public Fields getFields() {
        return fields;
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

    public String issueId() {
        return fields.getValue("issueId");
    }

    public String contentProviderFormatId() {
        return fields.getRequiredValue("contentProviderFormatId");
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PendingLoan)) {
            return false;
        }
        final PendingLoan rhs = (PendingLoan) obj;
        return new EqualsBuilder().append(lmsRecordId(), rhs.lmsRecordId()).append(contentProviderAlias(), rhs.contentProviderAlias())
                .append(contentProviderRecordId(), rhs.contentProviderRecordId()).append(issueId(), rhs.issueId())
                .append(contentProviderFormatId(), rhs.contentProviderFormatId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(lmsRecordId()).append(contentProviderAlias()).append(contentProviderRecordId())
                .append(issueId()).append(contentProviderFormatId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
