/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common.language;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import  jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Defines a supported language in the eHUB.
 */
@Entity
@Table(name = "LANGUAGE")
@Access(AccessType.PROPERTY)
public class Language implements Serializable {
    private String id;

    /**
     * Default constructor required by JPA.
     */
    public Language() {
    }

    public Language(final String id) {
        this.id = id;
    }

    /**
     * Returns the ID of the {@link Language}, i.e. the ISO 639 alpha-2 or alpha-3 language code.
     *
     * @return an  ISO 639 alpha-2 or alpha-3 language code
     */
    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the {@link Language}, i.e. the ISO 639 alpha-2 or alpha-3 language code.
     *
     * @param id the ISO 639 alpha-2 or alpha-3 language code to set
     */
    public void setId(String id) {
        this.id = id;
    }

    @Transient
    public String getDisplayName(final Locale locale) {
        return id == null ? null : new Locale(id).getDisplayName(locale);
    }


    public static List<Language> sort(final List<Language> languages, final Locale locale) {
        List<Language> sortedLanguages = Lists.newArrayList(languages);
        Collections.sort(sortedLanguages, new LanguageComparator(locale));
        return sortedLanguages;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Language rhs = (Language) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(getId()).toHashCode();
    }

    private static class LanguageComparator implements Comparator<Language>, Serializable {
        private Locale locale;

        private LanguageComparator(final Locale locale) {
            this.locale = locale;
        }

        @Override
        public int compare(final Language language1, final Language language2) {
            return language1.getDisplayName(locale).compareTo(language2.getDisplayName(locale));
        }
    }
}
