/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import java.util.ArrayList;
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
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;

/**
 * Represents a consumer of the eHUB. An {@link EhubConsumer} can have many {@link ContentProviderConsumer}s, i.e.
 * consumer specific configurations for the different {@link ContentProvider}s.
 */
@Entity
@Table(name = "EHUB_CONSUMER")
@Access(AccessType.PROPERTY)
public class EhubConsumer extends AbstractPersistable<Long> {
    private static final long serialVersionUID = 1365720155205061749L;
    private String description;
    private String secretKey;
    private Map<EhubConsumerPropertyKey, String> properties;
    private Set<ContentProviderConsumer> contentProviderConsumers;

    /**
     * Constructs a new empty {@link EhubConsumer}.
     */
    public EhubConsumer() {
    }

    /**
     * Constructs a new {@link EhubConsumer}.
     * 
     * @param description the description of the {@link EhubConsumer}
     * @param secretKey the secret key of the {@link EhubConsumer}
     * @param properties the {@link EhubConsumer} properties
     */
    public EhubConsumer(final String description, final String secretKey, final Map<EhubConsumerPropertyKey, String> properties) {
        this.description = description;
        this.secretKey = secretKey;
        this.properties = properties;
    }

    /**
     * Returns the description of the {@link EhubConsumer}.
     * 
     * @return the description of the {@link EhubConsumer}
     */
    @Column(name = "DESCRIPTION", nullable = false, unique = true)
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the {@link EhubConsumer}.
     * 
     * @param description the description of the {@link EhubConsumer} to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the secret key of the {@link EhubConsumer}.
     * 
     * @return the secret key of the {@link EhubConsumer}
     */
    @Column(name = "SECRET_KEY", nullable = false, unique = false)
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the secret key of the {@link EhubConsumer}.
     * 
     * @param secretKey the secret key of the {@link EhubConsumer} to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Returns the {@link ContentProviderConsumer}s this {@link EhubConsumer} has.
     * 
     * @return a {@link Set} of {@link ContentProviderConsumer}s
     */
    @OneToMany(mappedBy = "ehubConsumer", fetch = FetchType.LAZY)
    public Set<ContentProviderConsumer> getContentProviderConsumers() {
        return contentProviderConsumers;
    }

    /**
     * Sets the {@link ContentProviderConsumer}s this {@link EhubConsumer} has.
     * 
     * @param contentProviderConsumers the {@link ContentProviderConsumer}s this {@link EhubConsumer} has to set
     */
    public void setContentProviderConsumers(final Set<ContentProviderConsumer> contentProviderConsumers) {
        this.contentProviderConsumers = contentProviderConsumers;
    }

    /**
     * Returns the {@link ContentProviderConsumer}s this {@link EhubConsumer} has as a {@link List}.
     * 
     * @return a {@link List} of {@link ContentProviderConsumer}s
     */
    @Transient
    public List<ContentProviderConsumer> getContentProviderConsumersAsList() {
        return contentProviderConsumers == null ? new ArrayList<ContentProviderConsumer>() : new ArrayList<ContentProviderConsumer>(contentProviderConsumers);
    }

    /**
     * Gets the {@link ContentProviderConsumer} for a given {@link ContentProviderName}.
     * 
     * @param contentProviderName content provider name.
     * @return content consumer
     * @throws X if no {@link ContentProviderConsumer} could be found with the given provider name
     */
    @Transient
    public final ContentProviderConsumer getContentProviderConsumer(final ContentProviderName contentProviderName) {
        for (ContentProviderConsumer contentProviderConsumer : contentProviderConsumers) {
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
            if (contentProvider.getName().equals(contentProviderName)) {
                return contentProviderConsumer;
            }
        }

        final ErrorCauseArgument nameArg = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, contentProviderName);
        final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, getId());
        throw new NotFoundException(ErrorCause.CONTENT_PROVIDER_CONSUMER_NOT_FOUND, nameArg, ehubConsumerIdArg);
    }

    /**
     * Gets a list of {@link ContentProvider}s for which this {@link EhubConsumer} has no
     * {@link ContentProviderConsumer}.
     * 
     * @param allContentProviders a list of all available {@link ContentProvider}s in the eHUB
     * @return a list of {@link ContentProvider}s
     */
    @Transient
    List<ContentProvider> getRemainingContentProviders(final List<ContentProvider> allContentProviders) {
        final Set<ContentProviderConsumer> consumers = getContentProviderConsumers();
        final Set<ContentProvider> providers = new HashSet<>();
        for (ContentProviderConsumer consumer : consumers) {
            ContentProvider provider = consumer.getContentProvider();
            providers.add(provider);
        }

        final List<ContentProvider> remainingProviders = new ArrayList<>();
        for (ContentProvider provider : allContentProviders) {
            if (!providers.contains(provider)) {
                remainingProviders.add(provider);
            }
        }
        return remainingProviders;
    }

    /**
     * Returns the {@link EhubConsumer} properties.
     * 
     * @return the {@link EhubConsumer} properties
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "EHUB_CONSUMER_PROPERTY", joinColumns = @JoinColumn(name = "EHUB_CONSUMER_ID"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "PROPERTY_KEY", nullable = false)
    @Column(name = "PROPERTY_VALUE")
    @ForeignKey(name = "EHUB_CONSUMER_P_EHUB_C_FK")
    public Map<EhubConsumerPropertyKey, String> getProperties() {
        return properties;
    }

    /**
     * Sets the {@link EhubConsumer} properties.
     * 
     * @param properties the {@link EhubConsumer} properties
     */
    public void setProperties(final Map<EhubConsumerPropertyKey, String> properties) {
        this.properties = properties;
    }

    /**
     * Gets the value of a property with the given key.
     * 
     * @param key the key of the property
     * @return the property value
     * @throws IllegalArgumentException if there exists no property with the given name
     */
    @Transient
    final String getProperty(final EhubConsumerPropertyKey key) {
        return getProperties().get(key);
    }

    /**
     * Enumeration for ehub consumer property keys.
     */
    public static enum EhubConsumerPropertyKey {
        ARENA_PALMA_URL, ARENA_AGENCY_M_IDENTIFIER
    }
}
