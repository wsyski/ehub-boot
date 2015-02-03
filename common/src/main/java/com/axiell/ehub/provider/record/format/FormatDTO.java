package com.axiell.ehub.provider.record.format;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class FormatDTO {
    private String id;
    private String name;
    private String description;
    private ContentDisposition contentDisposition;

    public String getId() {
        return id;
    }

    public FormatDTO id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FormatDTO name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public FormatDTO contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public FormatDTO description(String description) {
        this.description = description;
        return this;
    }
}
