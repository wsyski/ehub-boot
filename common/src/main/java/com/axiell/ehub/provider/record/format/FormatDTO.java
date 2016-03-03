package com.axiell.ehub.provider.record.format;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatDTO implements Serializable {
    private String id;
    private String name;
    private String description;
    private ContentDisposition contentDisposition;
    private int playerWidth;
    private int playerHeight;

    public FormatDTO() {
    }

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

    public FormatDTO description(String description) {
        this.description = description;
        return this;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public FormatDTO contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public int getPlayerWidth() {
        return playerWidth;
    }

    public FormatDTO playerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
        return this;
    }

    public int getPlayerHeight() {
        return playerHeight;
    }

    public FormatDTO playerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
        return this;
    }
}
