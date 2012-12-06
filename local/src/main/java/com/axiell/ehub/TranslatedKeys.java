/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.wicket.Component;

/**
 * 
 */
public final class TranslatedKeys<K> extends ArrayList<TranslatedKey<K>> {
    private static final long serialVersionUID = -1566108297572631225L;

    /**
     * Constructs a new {@link TranslatedKeys}
     * 
     * @param component the component owning the translation of the key
     * @param map a {@link Map} containing the keys and the corresponding values
     */
    public TranslatedKeys(final Component component, final Map<K, String> map) {
        this(component, map.keySet());
    }
 
    /**
     * Constructs a new {@link TranslatedKeys}
     * 
     * @param component the component owning the translation of the key
     * @param keys a {@link Collection} of keys
     */
    public TranslatedKeys(final Component component, final Collection<K> keys) {
        super();
        for (K key : keys) {
            TranslatedKey<K> translatedKey = new TranslatedKey<K>(component, key);
            add(translatedKey);
        }
        Collections.sort(this);
    }
}
