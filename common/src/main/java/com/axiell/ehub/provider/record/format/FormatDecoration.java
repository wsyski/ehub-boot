/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.provider.ContentProvider;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.Locale;
import java.util.Map;

/**
 * Represents decorations of a specific format at a specific {@link ContentProvider}. These decorations are kept in the
 * eHUB.
 */
@Entity
@Table(name = "CONTENT_P_FORMAT_DECORATION", uniqueConstraints = @UniqueConstraint(columnNames = {"CONTENT_PROVIDER_ID", "FORMAT_ID"}))
@Access(AccessType.PROPERTY)
public class FormatDecoration extends AbstractTimestampAwarePersistable<Long> {
    private static final long serialVersionUID = 1562910983744673362L;
    private ContentProvider contentProvider;
    private String contentProviderFormatId;
    private ContentDisposition contentDisposition;
    private int playerWidth;
    private int playerHeight;
    private Map<String, FormatTextBundle> textBundles;

    /**
     * Empty constructor required by JPA.
     */
    protected FormatDecoration() {
    }

    /**
     * Constructs a new {@link FormatDecoration}
     *
     * @param contentProvider the {@link ContentProvider} this {@link FormatDecoration} belongs to
     */
    public FormatDecoration(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Constructs a new {@link FormatDecoration}.
     *
     * @param contentProvider         the {@link ContentProvider} this {@link FormatDecoration} belongs to
     * @param contentProviderFormatId the ID of the format at the {@link ContentProvider}
     * @param contentDisposition      the {@link ContentDisposition} for the specified format
     */
    public FormatDecoration(ContentProvider contentProvider, String contentProviderFormatId, ContentDisposition contentDisposition, int playerWidth,
                            int playerHeight) {
        this.contentProvider = contentProvider;
        this.contentProviderFormatId = contentProviderFormatId;
        this.contentDisposition = contentDisposition;
    }

    /**
     * Gets the {@link ContentProvider} this {@link FormatDecoration} belongs to.
     *
     * @return a {@link ContentProvider}
     */
    @ManyToOne
    @JoinColumn(name = "CONTENT_PROVIDER_ID", nullable = false)
    @ForeignKey(name = "FK_CONTENT_P_F_D_CONTENT_P")
    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    /**
     * Sets the {@link ContentProvider} this {@link FormatDecoration} belongs to.
     *
     * @param contentProvider the {@link ContentProvider} this {@link FormatDecoration} belongs to
     */
    protected void setContentProvider(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Returns the ID of the format at the {@link ContentProvider}.
     *
     * @return the ID of the format at the {@link ContentProvider}
     */
    @Column(name = "FORMAT_ID", nullable = false)
    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }

    /**
     * Sets the ID of the format at the {@link ContentProvider}.
     *
     * @param contentProviderFormatId the ID of the format at the {@link ContentProvider} to set
     */
    protected void setContentProviderFormatId(String contentProviderFormatId) {
        this.contentProviderFormatId = contentProviderFormatId;
    }

    /**
     * Returns the {@link ContentDisposition} for the specified format.
     *
     * @return a {@link ContentDisposition}
     */
    @Column(name = "CONTENT_DISPOSITION", nullable = false)
    @Enumerated(EnumType.STRING)
    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    /**
     * Sets the {@link ContentDisposition} for the specified format.
     *
     * @param contentDisposition the {@link ContentDisposition} for the specified format to set
     */
    protected void setContentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    /**
     * Returns the width of the player in pixels.
     *
     * @return the number of pixels
     */
    @Column(name = "PLAYER_WIDTH", nullable = false)
    public int getPlayerWidth() {
        return playerWidth;
    }

    /**
     * Sets the width of the player in pixels.
     *
     * @param playerWidth the width of the player in pixels to set
     */
    public void setPlayerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
    }

    /**
     * Returns the height of the player in pixels.
     *
     * @return the number of pixels
     */
    @Column(name = "PLAYER_HEIGHT", nullable = false)
    public int getPlayerHeight() {
        return playerHeight;
    }

    /**
     * Sets the height of the player in pixels
     *
     * @param playerHeight the height of the player in pixels to set
     */
    public void setPlayerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
    }

    /**
     * Returns the {@link FormatTextBundle}s.
     *
     * @return the {@link FormatTextBundle}s
     */
    @OneToMany(mappedBy = "formatDecoration", fetch = FetchType.LAZY)
    @MapKey(name = "language")
    public Map<String, FormatTextBundle> getTextBundles() {
        return textBundles;
    }

    /**
     * Sets the {@link FormatTextBundle}s.
     *
     * @param textBundles the {@link FormatTextBundle}s to set
     */
    public void setTextBundles(Map<String, FormatTextBundle> textBundles) {
        this.textBundles = textBundles;
    }

    /**
     * Gets a {@link FormatTextBundle} in the specified language. If no {@link FormatTextBundle} exists in the chosen
     * language the {@link FormatTextBundle} in the default language (English) is returned.
     *
     * @param language the language as an ISO 639 alpha-2 or alpha-3 language code to get the {@link FormatTextBundle}
     *                 in
     * @return a {@link FormatTextBundle}, <code>null</code> if no {@link FormatTextBundle}s could be found for this
     *         {@link FormatDecoration}
     */
    @Transient
    public final FormatTextBundle getTextBundle(final String language) {
        Validate.notNull(language, "The language can't be null");
        final Map<String, FormatTextBundle> textBundles = getTextBundles();

        if (textBundles == null) {
            return null;
        }

        final String lowerCaseLanguage = language.toLowerCase();
        final FormatTextBundle textBundle = textBundles.get(lowerCaseLanguage);

        if (textBundle == null) {
            // Get the default text in English if no text bundle exists in the chosen language
            final String defaultLanguage = Locale.ENGLISH.getLanguage();
            return textBundles.get(defaultLanguage);
        } else {
            return textBundle;
        }
    }

    /**
     * Defines the disposition of a content.
     */
    public static enum ContentDisposition {
        /**
         * Indicates that the content will be downloaded.
         */
        DOWNLOADABLE,
        /**
         * Indicates that the content will be streamed.
         */
        STREAMING;
    }
}
