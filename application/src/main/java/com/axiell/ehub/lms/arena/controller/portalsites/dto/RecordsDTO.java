package com.axiell.ehub.lms.arena.controller.portalsites.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordsDTO {
    private int total;
    private List<RecordDTO> records = new ArrayList<>();

    private RecordsDTO() {
    }

    public RecordsDTO(final int total, final List<RecordDTO> records) {
        this.total = total;
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public List<RecordDTO> getRecords() {
        return records;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecordsDTO)) {
            return false;
        }
        final RecordsDTO rhs = (RecordsDTO) obj;
        return new EqualsBuilder().append(total, rhs.total).append(records, rhs.records).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(total).append(records).toHashCode();
    }

}
