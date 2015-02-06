/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

public class Format {
    private final FormatDTO dto;

    public Format(FormatDTO formatDTO) {
        dto = formatDTO;
    }

    public Format(String formatId, String name, String description, ContentDisposition contentDisposition, int playerWidth, int playerHeight) {
        dto = new FormatDTO().id(formatId).name(name).description(description).contentDisposition(contentDisposition).playerWidth(playerWidth).playerHeight(playerHeight);
    }

    public String id() {
        return dto.getId();
    }

    public String name() {
        return dto.getName();
    }

    public String description() {
        return dto.getDescription();
    }

    public ContentDisposition contentDisposition() {
        return dto.getContentDisposition();
    }

    public int playerWidth() {
        return dto.getPlayerWidth();
    }

    public int playerHeight() {
        return dto.getPlayerHeight();
    }

    public FormatDTO toDTO() {
        return dto;
    }
}
