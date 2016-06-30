package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.issue.Issue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Record_v3 implements Serializable {
    private final RecordDTO_v3 recordDTO;

    public Record_v3(final RecordDTO_v3 recordDTO) {
        this.recordDTO = recordDTO;
    }

    public String id() {
        return recordDTO.getId();
    }

    public List<Issue> issues() {
        return recordDTO.getIssues().stream().map(Issue::new).collect(Collectors.toList());
    }

    public RecordDTO_v3 toDTO() {
        return recordDTO;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Record_v3)) {
            return false;
        }
        final Record_v3 rhs = (Record_v3) obj;
        return new EqualsBuilder().append(toDTO(), rhs.toDTO()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(toDTO()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
