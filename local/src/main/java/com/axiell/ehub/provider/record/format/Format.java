package com.axiell.ehub.provider.record.format;

public class Format {
    private String formatId;
    private String name;
    private String description;
    private String iconUrl;

    public Format(String formatId, String name, String description, String iconUrl) {
        this.formatId = formatId;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return formatId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    // TODO Fix hardcoded content disposition
    public FormatDTO toDTO() {
        return new FormatDTO().id(getId()).description(getDescription()).name(getName()).contentDisposition(ContentDisposition.DOWNLOADABLE);
    }
}
