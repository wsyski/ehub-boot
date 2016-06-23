package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDTO implements Serializable {
    private String id;
    private String title;
    private String imageUrl;
    private List<FormatDTO> formats = Lists.newArrayList();

    private IssueDTO() {
    }

    public IssueDTO(final List<FormatDTO> formats) {
        this.formats = formats;
    }

    public String getId() {
        return id;
    }

    public IssueDTO id(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public IssueDTO title(final String name) {
        this.title = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public IssueDTO imageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public List<FormatDTO> getFormats() {
        return formats;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueDTO)) {
            return false;
        }
        final IssueDTO rhs = (IssueDTO) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).append(getFormats(), rhs.getFormats()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getId()).append(getFormats()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
