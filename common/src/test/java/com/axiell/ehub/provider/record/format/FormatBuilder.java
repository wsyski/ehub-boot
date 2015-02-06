package com.axiell.ehub.provider.record.format;

public class FormatBuilder {
    public static final String FORMAT_ID = "1";
    public static final String FORMAT_NAME = "formatName";
    public static final String FORMAT_DESCRIPTION = "formatDescription";
    public static final ContentDisposition DOWNLOADABLE_CONTENT_DISPOSITION = ContentDisposition.DOWNLOADABLE;
    public static final ContentDisposition STREAMING_CONTENT_DISPOSITION = ContentDisposition.STREAMING;
    public static final int PLAYER_WIDTH = 10;
    public static final int PLAYER_HEIGHT = 20;

    public static Format downloadableFormat() {
        return new Format(FORMAT_ID, FORMAT_NAME, FORMAT_DESCRIPTION, DOWNLOADABLE_CONTENT_DISPOSITION, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public static Format streamingFormat() {
        return new Format(FORMAT_ID, FORMAT_NAME, FORMAT_DESCRIPTION, STREAMING_CONTENT_DISPOSITION, PLAYER_WIDTH, PLAYER_HEIGHT);
    }
}
