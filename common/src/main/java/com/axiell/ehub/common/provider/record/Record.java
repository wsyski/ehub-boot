package com.axiell.ehub.common.provider.record;

import com.axiell.ehub.common.controller.external.v5_0.provider.dto.RecordDTO;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.IssueDTO;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Record implements Serializable {
    private final RecordDTO recordDTO;

    public Record(final String id, final List<Issue> issues) {
        Validate.notNull(issues);
        List<IssueDTO> issuesDTO = issues.stream().map(Issue::toDTO).collect(Collectors.toList());
        recordDTO = new RecordDTO(id, issuesDTO);
    }

    public Record(final RecordDTO recordDTO) {
        this.recordDTO = recordDTO;
    }

    public String getId() {
        return recordDTO.getId();
    }

    public List<Issue> getIssues() {
        return recordDTO.getIssues().stream().map(Issue::new).collect(Collectors.toList());
    }

    public RecordDTO toDTO() {
        return recordDTO;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Record)) {
            return false;
        }
        final Record rhs = (Record) obj;
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
