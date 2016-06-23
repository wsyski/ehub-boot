package com.axiell.ehub.provider.record.format;

import java.util.Collections;
import java.util.Set;

public class FormatBuilder {
    public static final String FORMAT_ID = "1";
    public static final String FORMAT_NAME = "formatName";
    public static final String FORMAT_DESCRIPTION = "formatDescription";
    public static final ContentDisposition DOWNLOADABLE_CONTENT_DISPOSITION = ContentDisposition.DOWNLOADABLE;
    public static final ContentDisposition STREAMING_CONTENT_DISPOSITION = ContentDisposition.STREAMING;
    public static final String PLATFORM_PCMAC = "PCMAC";
    public static final Set<String> PLATFORMS = Collections.singleton(PLATFORM_PCMAC);

    public static Format downloadableFormat() {
        return new Format(FORMAT_ID, FORMAT_NAME, FORMAT_DESCRIPTION, DOWNLOADABLE_CONTENT_DISPOSITION, PLATFORMS, false);
    }

    public static Format streamingFormat() {
        return new Format(FORMAT_ID, FORMAT_NAME, FORMAT_DESCRIPTION, STREAMING_CONTENT_DISPOSITION, PLATFORMS, false);
    }
}
