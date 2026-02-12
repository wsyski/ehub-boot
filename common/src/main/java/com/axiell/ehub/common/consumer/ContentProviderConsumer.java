package com.axiell.ehub.common.consumer;

import com.axiell.ehub.common.AbstractTimestampAwarePersistable;
import com.axiell.ehub.common.provider.ContentProvider;
import com.google.common.collect.ImmutableMap;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_ID;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_KEY;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_TOKEN_EXPIRATION_TIME_IN_SECONDS;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDIRVE_WEBSITE_ID;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_KEY;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_SECRET;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_ERROR_PAGE_URL;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_ILS_NAME;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_LIBRARY_ID;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_READ_AUTH_URL;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Represents a consumer of a specific {@link ContentProvider}. It holds the
 * consumer specific parameters used when connecting to a certain
 * {@link ContentProvider}.
 */
@Entity
@Table(name = "CONTENT_PROVIDER_CONSUMER")
@Access(AccessType.PROPERTY)
public class ContentProviderConsumer extends AbstractTimestampAwarePersistable<Long> implements Serializable {
    private static final Map<String, Set<ContentProviderConsumerPropertyKey>> VALID_PROPERTY_KEYS =
            ImmutableMap.<String, Set<ContentProviderConsumerPropertyKey>>builder()
                    .put(ContentProvider.CONTENT_PROVIDER_ELIB3, newHashSet(ELIB_SERVICE_ID, ELIB_SERVICE_KEY))
                    .put(ContentProvider.CONTENT_PROVIDER_OVERDRIVE,
                            newHashSet(OVERDRIVE_CLIENT_KEY, OVERDRIVE_CLIENT_SECRET, OVERDRIVE_LIBRARY_ID, OVERDRIVE_ERROR_PAGE_URL,
                                    OVERDRIVE_READ_AUTH_URL, OVERDIRVE_WEBSITE_ID, OVERDRIVE_ILS_NAME))
                    .build();

    private static final Set<ContentProviderConsumerPropertyKey> EP_VALID_PROPERTY_KEYS = newHashSet(EP_SITE_ID, EP_SECRET_KEY, EP_TOKEN_EXPIRATION_TIME_IN_SECONDS);
    private static final Map<ContentProviderConsumerPropertyKey, Pattern> PROPERTY_PATTERNS =
            ImmutableMap.<ContentProviderConsumerPropertyKey, Pattern>builder()
                    .put(EP_TOKEN_EXPIRATION_TIME_IN_SECONDS, Pattern.compile("\\d+")).build();

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
     * @param ehubConsumer    the {@link EhubConsumer}
     * @param contentProvider the {@link ContentProvider}
     * @param properties      {@link ContentProviderConsumer} properties
     */
    public ContentProviderConsumer(final EhubConsumer ehubConsumer, final ContentProvider contentProvider,
                                   final Map<ContentProviderConsumerPropertyKey, String> properties) {
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
    @JoinColumn(name = "EHUB_CONSUMER_ID", nullable = false)
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
    @JoinColumn(name = "CONTENT_PROVIDER_ID", nullable = false)
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
    @ElementCollection
    @CollectionTable(name = "CONTENT_P_CONSUMER_PROPERTY", joinColumns = @JoinColumn(name = "CONTENT_PROVIDER_CONSUMER_ID"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "PROPERTY_KEY", nullable = false)
    @Column(name = "PROPERTY_VALUE")
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
        Set<ContentProviderConsumerPropertyKey> keys = VALID_PROPERTY_KEYS.get(contentProvider.getName());
        return new ArrayList<>(keys == null ? EP_VALID_PROPERTY_KEYS : keys);
    }

    /**
     * Gets the value of a property with the given key.
     *
     * @param key the key of the property
     * @return the property value
     * @throws NullPointerException     if this {@link ContentProviderConsumer} has no valid property
     *                                  keys
     * @throws IllegalArgumentException if there exists no property with the given key among the
     *                                  valid property keys
     */
    @Transient
    public String getProperty(final ContentProviderConsumerPropertyKey key) {
        List<ContentProviderConsumerPropertyKey> validPropertyKeys = getValidPropertyKeys();
        Validate.notNull(validPropertyKeys, "Valid property keys can't be null");
        Validate.isTrue(validPropertyKeys.contains(key), "Invalid property key: " + key + " for provider: " + contentProvider.getName());
        return getProperties().get(key);
    }

    @Transient
    public Pattern getPropertyValidatorPattern(final ContentProviderConsumerPropertyKey contentProviderConsumerPropertyKey) {
        return PROPERTY_PATTERNS.get(contentProviderConsumerPropertyKey);
    }

    public enum ContentProviderConsumerPropertyKey {
        OVERDRIVE_CLIENT_KEY, OVERDRIVE_CLIENT_SECRET, OVERDRIVE_LIBRARY_ID, OVERDRIVE_ERROR_PAGE_URL, OVERDRIVE_READ_AUTH_URL, OVERDIRVE_WEBSITE_ID,
        OVERDRIVE_ILS_NAME, ELIB_SERVICE_ID, ELIB_SERVICE_KEY,
        EP_SITE_ID, EP_SECRET_KEY, EP_TOKEN_EXPIRATION_TIME_IN_SECONDS, EP_TOKEN_LEEWAY_IN_SECONDS
    }
}
