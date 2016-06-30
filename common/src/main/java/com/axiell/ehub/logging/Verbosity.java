/**
 * {@code Verbosity} determines how detailed message will be logged.
 * <p>
 * <ul>
 * <li>The lowest verbosity ({@link #HEADERS_ONLY}) will log only request/response headers.</li>
 * <li>
 * The medium verbosity will log request/response headers, as well as an entity if considered a readable text. See {@link
 * #PAYLOAD_TEXT}.
 * </li>
 * <li>The highest verbosity will log all types of an entity (besides the request/response headers.</li>
 * </ul>
 * <p>
 * Note that the entity is logged up to the maximum number specified in any of the following constructors {@link
 * LoggingFeature#LoggingFeature(Logger, Integer)}, {@link LoggingFeature#LoggingFeature(Logger, Level, Verbosity, Integer)}
 * or by some of the feature's properties (see {@link #LOGGING_FEATURE_MAX_ENTITY_SIZE}, {@link
 * #LOGGING_FEATURE_MAX_ENTITY_SIZE_CLIENT}, {@link #LOGGING_FEATURE_MAX_ENTITY_SIZE_SERVER}.
 */
package com.axiell.ehub.logging;

public enum Verbosity {
    /**
     * Only content of HTTP headers is logged. No message payload data are logged.
     */
    HEADERS_ONLY,
    /**
     * Content of HTTP headers as well as entity content of textual media types is logged. Following is the list of media
     * types that are considered textual for the logging purposes:
     * <ul>
     * <li>{@code text/*}</li>
     * <li>{@code application/atom+xml}</li>
     * <li>{@code application/json}</li>
     * <li>{@code application/svg+xml}</li>
     * <li>{@code application/x-www-form-urlencoded}</li>
     * <li>{@code application/xhtml+xml}</li>
     * <li>{@code application/xml}</li>
     * </ul>
     */
    PAYLOAD_TEXT,
    /**
     * Full verbose logging. Content of HTTP headers as well as any message payload content will be logged.
     */
    PAYLOAD_ANY
}

