/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local;

import org.apache.wicket.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 *
 */
public final class TranslatedKeys<K> extends ArrayList<TranslatedKey<K>> {

    /**
     * Constructs a new {@link TranslatedKeys}
     *
     * @param component the component owning the translation of the key
     * @param map       a {@link Map} containing the keys and the corresponding values
     */
    public TranslatedKeys(final Component component, final Map<K, String> map) {
        this(component, map.keySet());
    }

    /**
     * Constructs a new {@link TranslatedKeys}
     *
     * @param component the component owning the translation of the key
     * @param keys      a {@link Collection} of keys
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
