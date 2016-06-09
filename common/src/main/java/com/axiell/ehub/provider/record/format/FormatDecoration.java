/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.platform.Platform;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents decorations of a specific format at a specific
 * {@link ContentProvider}. These decorations are kept in the eHUB.
 */
@Entity
@Table(name = "CONTENT_P_FORMAT_DECORATION", uniqueConstraints = @UniqueConstraint(columnNames = {"CONTENT_PROVIDER_ID", "FORMAT_ID"},
        name = "UK_CONTENT_P_FORMAT_DECORATION"))
@Access(AccessType.PROPERTY)
public class FormatDecoration extends AbstractTimestampAwarePersistable<Long> {
    private static final long serialVersionUID = 1562910983744673362L;
    private ContentProvider contentProvider;
    private String contentProviderFormatId;
    private boolean isLocked;
    private ContentDisposition contentDisposition;
    private Map<Language, FormatTextBundle> textBundles;
    private Set<Platform> platforms;

    /**
     * Empty constructor required by JPA.
     */
    public FormatDecoration() {
    }

    /**
     * Constructs a new {@link FormatDecoration}
     *
     * @param contentProvider the {@link ContentProvider} this {@link FormatDecoration}
     *                        belongs to
     */
    public FormatDecoration(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Constructs a new {@link FormatDecoration}.
     *
     * @param contentProvider         the {@link ContentProvider} this {@link FormatDecoration}
     *                                belongs to
     * @param contentProviderFormatId the ID of the format at the {@link ContentProvider}
     * @param contentDisposition      the {@link ContentDisposition} for the specified format
     * @param platforms               the supported player platforms
     */
    public FormatDecoration(ContentProvider contentProvider, String contentProviderFormatId, ContentDisposition contentDisposition, Set<Platform> platforms) {
        this.contentProvider = contentProvider;
        this.contentProviderFormatId = contentProviderFormatId;
        this.contentDisposition = contentDisposition;
        this.platforms = platforms;
    }

    @Column(name = "LOCKED", nullable = false)
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(final boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * Gets the {@link ContentProvider} this {@link FormatDecoration} belongs
     * to.
     *
     * @return a {@link ContentProvider}
     */
    @ManyToOne
    @JoinColumn(name = "CONTENT_PROVIDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CONTENT_P_F_D_CONTENT_P"))
    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    /**
     * Sets the {@link ContentProvider} this {@link FormatDecoration} belongs
     * to.
     *
     * @param contentProvider the {@link ContentProvider} this {@link FormatDecoration}
     *                        belongs to
     */
    public void setContentProvider(ContentProvider contentProvider) {
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
    public void setContentProviderFormatId(String contentProviderFormatId) {
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
    public void setContentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    /**
     * Returns the {@link FormatTextBundle}s.
     *
     * @return the {@link FormatTextBundle}s
     */
    @OneToMany(mappedBy = "formatDecoration", cascade = CascadeType.REMOVE)
    @MapKeyJoinColumn(name = "LANGUAGE_ID", foreignKey = @ForeignKey(name = "FK_CONTENT_P_F_T_B_CONTENT_P_F_T"))
    public Map<Language, FormatTextBundle> getTextBundles() {
        return textBundles;
    }

    /**
     * Sets the {@link FormatTextBundle}s.
     *
     * @param textBundles the {@link FormatTextBundle}s to set
     */
    public void setTextBundles(Map<Language, FormatTextBundle> textBundles) {
        this.textBundles = textBundles;
    }

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "FORMAT_DECORATION_PLATFORM", joinColumns = @JoinColumn(name = "CONTENT_P_FORMAT_DECORATION_ID"), inverseJoinColumns = @JoinColumn(name = "PLATFORM_ID"), foreignKey = @ForeignKey(name = "FK_F_D_P_CONTENT_P_F_D"), inverseForeignKey = @ForeignKey(name = "FK_F_D_P_PLATFORM"))
    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(final Set<Platform> platforms) {
        this.platforms = platforms;
    }

    /**
     * Gets a {@link FormatTextBundle} in the specified language. If no
     * {@link FormatTextBundle} exists in the chosen language the
     * {@link FormatTextBundle} in the default language (English) is returned.
     *
     * @param language the language as an ISO 639 alpha-2 or alpha-3 language code to
     *                 get the {@link FormatTextBundle} in
     * @return a {@link FormatTextBundle}, <code>null</code> if no
     * {@link FormatTextBundle}s could be found for this
     * {@link FormatDecoration}
     */
    @Transient
    public FormatTextBundle getTextBundle(final String language) {
        Validate.notNull(language, "The language can't be null");
        final Map<Language, FormatTextBundle> bundles = getTextBundles();

        if (bundles == null) {
            return null;
        }

        final String lowerCaseLanguage = language.toLowerCase();
        final FormatTextBundle textBundle = bundles.get(new Language(lowerCaseLanguage));

        if (textBundle == null) {
            // Get the default text in English if no text bundle exists in the
            // chosen language
            final String defaultLanguage = Locale.ENGLISH.getLanguage();
            return bundles.get(new Language(defaultLanguage));
        } else {
            return textBundle;
        }
    }

    @Transient
    public Set<String> getPlatformNames() {
        return getPlatforms().stream().map(Platform::getName).collect(Collectors.toSet());
    }

}
