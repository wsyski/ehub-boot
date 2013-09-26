/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

/**
 * Represents a key that provides a translated label and a translated title. When it is compared to another
 * {@link TranslatedKey} it is the translated labels of the keys that are compared.
 */
public final class TranslatedKey<K> implements Comparable<TranslatedKey<K>>, Serializable {
    private static final long serialVersionUID = -6412303252692270087L;
    private transient Collator collator;
    private final K key;
    private final String label;
    private final String title;
    private final Locale locale;

    /**
     * Constructs a new {@link TranslatedKey}.
     *
     * @param component the component owning the translation of the key
     * @param key       the key
     */
    public TranslatedKey(final Component component, final K key) {
        locale = component.getLocale();
        this.key = key;
        final IModel<String> emptyModel = new Model<>();

        final StringResourceModel labelModel = new StringResourceModel(key.toString() + ".label", component, emptyModel);
        this.label = labelModel.getString();

        final StringResourceModel titleModel = new StringResourceModel(key.toString() + ".title", component, emptyModel);
        this.title = titleModel.getString();
    }

    /**
     * Returns the key.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the translated label.
     *
     * @return the translated label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the translated title.
     *
     * @return the translated title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TranslatedKey<K> key) {
        if (collator == null) {
            this.collator = Collator.getInstance(locale);
        }
        return collator.compare(label, key.getLabel());
    }
}
