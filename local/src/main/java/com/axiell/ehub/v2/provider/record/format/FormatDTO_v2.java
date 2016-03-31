package com.axiell.ehub.v2.provider.record.format;

import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatDTO_v2 implements Serializable {
    private String id;
    private String name;
    private String description;
    private ContentDisposition contentDisposition;
    private int playerWidth=0;
    private int playerHeight=0;

    public FormatDTO_v2() {
    }

    public String getId() {
        return id;
    }

    public FormatDTO_v2 id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FormatDTO_v2 name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FormatDTO_v2 description(String description) {
        this.description = description;
        return this;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public FormatDTO_v2 contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public int getPlayerWidth() {
        return playerWidth;
    }

    public FormatDTO_v2 playerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
        return this;
    }

    public int getPlayerHeight() {
        return playerHeight;
    }

    public FormatDTO_v2 playerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
        return this;
    }

    public static FormatDTO_v2 fromDTO(FormatDTO formatDTO) {
        return new FormatDTO_v2().id(formatDTO.getId()).name(formatDTO.getName()).description(formatDTO.getDescription()).contentDisposition(formatDTO.getContentDisposition()).playerWidth(0).playerHeight(0);
    }
}
