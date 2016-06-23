package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDTO;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Issue implements Serializable {
    private final IssueDTO dto;

    public Issue(final IssueDTO IssueDTO) {
        dto = IssueDTO;
    }

    public Issue(final String id, final String title, final String imageUrl, final List<Format> formats) {
        Validate.notNull(formats);
        List<FormatDTO> formatsDTO = formats.stream().map(Format::toDTO).collect(Collectors.toList());
        dto = new IssueDTO(formatsDTO).id(id).title(title).imageUrl(imageUrl);
    }

    public String getId() {
        return dto.getId();
    }

    public String getTitle() {
        return dto.getTitle();
    }

    public String getImageUrl() {
        return dto.getImageUrl();
    }

    public List<Format> getFormats() {
        return dto.getFormats().stream().map(Format::new).collect(Collectors.toList());
    }


    public IssueDTO toDTO() {
        return dto;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Issue)) {
            return false;
        }
        final Issue rhs = (Issue) obj;
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
