package com.axiell.ehub.common.provider.record.issue;

import com.axiell.ehub.common.controller.external.v5_0.provider.dto.IssueDTO;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.FormatDTO;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Issue implements Serializable {
    private final IssueDTO issueDTO;

    public Issue(final IssueDTO IssueDTO) {
        issueDTO = IssueDTO;
    }

    public Issue(final String id, final String title, final String imageUrl, final List<Format> formats) {
        Validate.notNull(formats);
        List<FormatDTO> formatsDTO = formats.stream().map(Format::toDTO).collect(Collectors.toList());
        issueDTO = new IssueDTO(formatsDTO).id(id).title(title).imageUrl(imageUrl);
    }

    public Issue(final List<Format> formats) {
        this(null, null, null, formats);
    }

    public String getId() {
        return issueDTO.getId();
    }

    public String getTitle() {
        return issueDTO.getTitle();
    }

    public String getImageUrl() {
        return issueDTO.getImageUrl();
    }

    public List<Format> getFormats() {
        return issueDTO.getFormats().stream().map(Format::new).collect(Collectors.toList());
    }

    public IssueDTO toDTO() {
        return issueDTO;
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
