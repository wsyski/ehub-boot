package com.axiell.ehub.provider.record.format;

/**
 * Defines the disposition of a content.
 */
public enum ContentDisposition {
    /**
     * Indicates that the getContent will be downloaded.
     */
    DOWNLOADABLE,
    /**
     * Indicates that the getContent will be streamed.
     */
    STREAMING
}
