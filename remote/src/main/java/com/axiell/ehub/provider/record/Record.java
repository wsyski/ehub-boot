package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.format.Format;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Record implements Serializable {
    private final RecordDTO recordDTO;

    public Record(final RecordDTO recordDTO) {
        this.recordDTO = recordDTO;
    }

    public String id() {
        return recordDTO.getId();
    }

    public List<Format> formats() {
        return recordDTO.getFormats().stream().map(Format::new).collect(Collectors.toList());
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
