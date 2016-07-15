package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Collections;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDTO {
    @JsonProperty("zinio_issue_id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("zinio_cover_image_url")
    private String imageUrl;

    private IssueDTO() {
    }

    public IssueDTO(final String id, final String title, final String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Issue toIssue(final Format format) {
        return new Issue(id, title, imageUrl, Collections.singletonList(format));
    }

    public static IssueDTO toIssueDTO(final Issue issue) {
        return new IssueDTO(issue.getId(), issue.getTitle(), issue.getImageUrl());
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
