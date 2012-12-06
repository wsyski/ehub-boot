/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_KEY;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.SUBSCRIPTION_ID;
import static com.axiell.ehub.provider.ContentProviderName.ELIB;
import static com.axiell.ehub.provider.ContentProviderName.ELIBU;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;

/**
 * Represents a consumer of a specific {@link ContentProvider}. It holds the consumer specific parameters used when
 * connecting to a certain {@link ContentProvider}.
 */
@Entity
@Table(name = "CONTENT_PROVIDER_CONSUMER")
@Access(AccessType.PROPERTY)
public class ContentProviderConsumer extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -2497784815429100591L;
    private final static Map<ContentProviderName, Set<ContentProviderConsumerPropertyKey>> VALID_PROPERTY_KEYS = new HashMap<>();

    static {
        VALID_PROPERTY_KEYS.put(ELIB, new HashSet<>(Arrays.asList(ELIB_RETAILER_ID, ELIB_RETAILER_KEY)));
        VALID_PROPERTY_KEYS.put(ELIBU, new HashSet<>(Arrays.asList(ELIBU_SERVICE_ID, ELIBU_SERVICE_KEY, SUBSCRIPTION_ID)));
    }

    private EhubConsumer ehubConsumer;
    private ContentProvider contentProvider;
    private Map<ContentProviderConsumerPropertyKey, String> properties;

    /**
     * Constructs a new empty {@link ContentProviderConsumer}.
     */
    public ContentProviderConsumer() {
    }

    /**
     * Constructs a new {@link ContentProviderConsumer}.
     * 
     * @param ehubConsumer the {@link EhubConsumer}
     * @param contentProvider the {@link ContentProvider}
     * @param properties {@link ContentProviderConsumer} properties
     */
    public ContentProviderConsumer(final EhubConsumer ehubConsumer, final ContentProvider contentProvider, final Map<ContentProviderConsumerPropertyKey, String> properties) {
        this.ehubConsumer = ehubConsumer;
        this.contentProvider = contentProvider;
        this.properties = properties;
    }

    /**
     * Gets the {@link EhubConsumer}
     * 
     * @return the {@link EhubConsumer}
     */
    @ManyToOne
    @JoinColumn(name = "EHUB_CONSUMER_ID")
    @ForeignKey(name = "CONTENT_P_C_EHUB_C_FK")
    public EhubConsumer getEhubConsumer() {
        return ehubConsumer;
    }

    /**
     * Sets the {@link EhubConsumer}.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to set
     */
    public void setEhubConsumer(final EhubConsumer ehubConsumer) {
        this.ehubConsumer = ehubConsumer;
    }

    /**
     * Returns the {@link ContentProvider}.
     * 
     * @return the {@link ContentProvider}
     */
    @ManyToOne
    @JoinColumn(name = "CONTENT_PROVIDER_ID")
    @ForeignKey(name = "CONTENT_P_C_CONTENT_P_FK")
    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    /**
     * Sets the {@link ContentProvider}.
     * 
     * @param contentProvider the {@link ContentProvider} to set
     */
    public void setContentProvider(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Gets the {@link ContentProviderConsumer} properties
     * 
     * @return the {@link ContentProviderConsumer} properties
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "CONTENT_P_CONSUMER_PROPERTY", joinColumns = @JoinColumn(name = "CONTENT_PROVIDER_CONSUMER_ID"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "PROPERTY_KEY", nullable = false)
    @Column(name = "PROPERTY_VALUE")
    @ForeignKey(name = "CONTENT_P_C_P_CONTENT_P_C_FK")
    public Map<ContentProviderConsumerPropertyKey, String> getProperties() {
        return properties;
    }

    /**
     * Sets the {@link ContentProviderConsumer} properties.
     * 
     * @param properties the {@link ContentProviderConsumer} properties to set
     */
    public void setProperties(final Map<ContentProviderConsumerPropertyKey, String> properties) {
        this.properties = properties;
    }

    /**
     * Gets the valid properties for this {@link ContentProviderConsumer}.
     * 
     * @return a {@link List} of {@link ContentProviderConsumerPropertyKey}s
     */
    @Transient
    public List<ContentProviderConsumerPropertyKey> getValidPropertyKeys() {
        Validate.notNull(contentProvider, "Content Provider can't be null");
        Set<ContentProviderConsumerPropertyKey> keySet = VALID_PROPERTY_KEYS.get(contentProvider.getName());
        return new ArrayList<>(keySet);
    }

    /**
     * Gets the value of a property with the given key.
     * 
     * @param key the key of the property
     * @return the property value
     * @throws NullPointerException if this {@link ContentProviderConsumer} has no valid property keys
     * @throws IllegalArgumentException if there exists no property with the given key among the valid property keys
     */
    @Transient
    public String getProperty(final ContentProviderConsumerPropertyKey key) {
        List<ContentProviderConsumerPropertyKey> validPropertyKeys = getValidPropertyKeys();
        Validate.notNull(validPropertyKeys, "Valid property keys can't be null");
        Validate.isTrue(validPropertyKeys.contains(key), "Invalid property key: " + key + " for provider: " + contentProvider.getName());
        return getProperties().get(key);
    }

    /**
     * Enumeration for content provider consumer property keys.
     */
    public static enum ContentProviderConsumerPropertyKey {
        ELIB_RETAILER_ID, ELIB_RETAILER_KEY, ELIBU_SERVICE_ID, ELIBU_SERVICE_KEY, SUBSCRIPTION_ID
    }
}
