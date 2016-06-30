package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.issue.IssueDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO_v3 implements Serializable {
    private String id;
    private List<IssueDTO> issues;

    public String getId() {
        return id;
    }

    public RecordDTO_v3 id(String id) {
        this.id = id;
        return this;
    }

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public RecordDTO_v3 issues(final List<IssueDTO> issues) {
        this.issues = issues;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecordDTO_v3)) {
            return false;
        }
        final RecordDTO_v3 rhs = (RecordDTO_v3) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
