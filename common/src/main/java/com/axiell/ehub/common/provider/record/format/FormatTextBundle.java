/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common.provider.record.format;

import com.axiell.ehub.common.AbstractTimestampAwarePersistable;
import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.ContentProvider;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serializable;

/**
 * Contains language specific texts related to a specific format at a {@link ContentProvider}.
 */
@Entity
@Table(name = "CONTENT_P_FORMAT_TEXT_BUNDLE", uniqueConstraints = @UniqueConstraint(columnNames = {"CONTENT_P_FORMAT_DECORATION_ID", "LANGUAGE_ID"}))
@Access(AccessType.PROPERTY)
public class FormatTextBundle extends AbstractTimestampAwarePersistable<Long> implements Serializable {
    private FormatDecoration formatDecoration;
    private Language language;
    private String name;
    private String description;

    /**
     * Empty constructor required by JPA.
     */
    public FormatTextBundle() {
    }

    /**
     * Constructs a new {@link FormatTextBundle}.
     *
     * @param formatDecoration the {@link FormatDecoration}
     * @param language         the language of the texts in this {@link FormatTextBundle} as an ISO 639 alpha-2
     *                         or alpha-3 language code
     * @param name             the name of the format in the specified language
     * @param description      the description of the format in the specified language
     */
    public FormatTextBundle(FormatDecoration formatDecoration, Language language, String name, String description) {
        this.formatDecoration = formatDecoration;
        this.language = language;
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the {@link FormatDecoration}.
     *
     * @return the {@link FormatDecoration}
     */
    @ManyToOne
    @JoinColumn(name = "CONTENT_P_FORMAT_DECORATION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_C_P_F_T_B_CONTENT_P_F_D"))
    public FormatDecoration getFormatDecoration() {
        return formatDecoration;
    }

    /**
     * Sets the {@link FormatDecoration}.
     *
     * @param formatDecoration the {@link FormatDecoration} to set
     */
    public void setFormatDecoration(FormatDecoration formatDecoration) {
        this.formatDecoration = formatDecoration;
    }

    /**
     * Returns the language of the texts in this {@link FormatTextBundle} as an ISO 639 alpha-2 or
     * alpha-3 language code.
     *
     * @return a language code
     */
    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CONTENT_P_F_T_B_LANGUAGE"))
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the language of this {@link FormatTextBundle} as an ISO 639 alpha-2 or alpha-3 language code.
     *
     * @param language the language of this {@link FormatTextBundle} as an ISO 639 alpha-2 or alpha-3
     *                 language code to set
     */
    public void setLanguage(final Language language) {
        this.language = language;
    }

    /**
     * Returns the name of the format in the specified language.
     *
     * @return the name of the format in the specified language
     */
    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the format in the specified language.
     *
     * @param name the name of the format in the specified language to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the format in the specified language.
     *
     * @return the description of the format in the specified language
     */
    @Column(name = "DESCRIPTION", nullable = false)
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the format in the specified language.
     *
     * @param description the description of the format in the specified language to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
