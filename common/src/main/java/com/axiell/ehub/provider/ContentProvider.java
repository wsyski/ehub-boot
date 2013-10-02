/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.CONSUME_LICENSE_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PRODUCT_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS;
import static com.axiell.ehub.provider.ContentProviderName.ASKEWS;
import static com.axiell.ehub.provider.ContentProviderName.ELIB;
import static com.axiell.ehub.provider.ContentProviderName.ELIBU;
import static com.axiell.ehub.provider.ContentProviderName.PUBLIT;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.eekboom.utils.Strings;

/**
 * Represents a Content Provider. It contains the common parameters for a certain Content Provider, e.g. the base URL of
 * the Content Provider's web services.
 */
@Entity
@Table(name = "CONTENT_PROVIDER")
@Access(AccessType.PROPERTY)
public class ContentProvider extends AbstractTimestampAwarePersistable<Long> {
    private static final long serialVersionUID = 3731023842900003678L;
    private final static Map<ContentProviderName, Set<ContentProviderPropertyKey>> VALID_PROPERTY_KEYS = new HashMap<>();

    static {
        VALID_PROPERTY_KEYS.put(ELIB, new HashSet<>(Arrays.asList(PRODUCT_URL, CREATE_LOAN_URL, ORDER_LIST_URL)));
        VALID_PROPERTY_KEYS.put(ELIBU, new HashSet<>(Arrays.asList(PRODUCT_URL, CONSUME_LICENSE_URL)));
        VALID_PROPERTY_KEYS.put(PUBLIT, new HashSet<>(Arrays.asList(PRODUCT_URL, CREATE_LOAN_URL, ORDER_LIST_URL, LOAN_EXPIRATION_DAYS)));
        VALID_PROPERTY_KEYS.put(ASKEWS, new HashSet<>(Arrays.asList(LOAN_EXPIRATION_DAYS)));
    }

    private ContentProviderName name;
    private Map<ContentProviderPropertyKey, String> properties;
    private Map<String, FormatDecoration> formatDecorations;

    /**
     * Empty constructor required by JPA
     */
    protected ContentProvider() {
    }

    /**
     * Constructs a new {@link ContentProvider}.
     * 
     * @param name the name of the {@link ContentProvider}
     * @param properties the {@link ContentProvider} properties
     */
    public ContentProvider(final ContentProviderName name, final Map<ContentProviderPropertyKey, String> properties) {
        this.name = name;
        this.properties = properties;
    }

    /**
     * Returns the name of the {@link ContentProvider}.
     * 
     * @return the name of the {@link ContentProvider}
     */
    @Column(name = "NAME", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    public ContentProviderName getName() {
        return name;
    }

    /**
     * Sets the name of the {@link ContentProvider}. Only used by JPA.
     * 
     * @param name the name of the {@link ContentProvider} to set
     */
    protected void setName(final ContentProviderName name) {
        this.name = name;
    }

    /**
     * Gets the {@link ContentProvider} properties.
     * 
     * @return the {@link ContentProvider} properties
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "CONTENT_PROVIDER_PROPERTY", joinColumns = @JoinColumn(name = "CONTENT_PROVIDER_ID"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "PROPERTY_KEY", nullable = false)
    @Column(name = "PROPERTY_VALUE")
    @ForeignKey(name = "FK_CONTENT_P_P_CONTENT_P")
    public Map<ContentProviderPropertyKey, String> getProperties() {
        return properties;
    }

    /**
     * Sets the {@link ContentProvider} properties. Only used by JPA.
     * 
     * @param properties the {@link ContentProvider} properties to set
     */
    protected void setProperties(Map<ContentProviderPropertyKey, String> properties) {
        this.properties = properties;
    }

    /**
     * Gets the valid properties for this {@link ContentProvider}.
     * 
     * @return a {@link List} of {@link ContentProviderPropertyKey}s
     */
    @Transient
    public List<ContentProviderPropertyKey> getValidPropertyKeys() {
        Validate.notNull(name, "Content Provider name can not be null");
        Set<ContentProviderPropertyKey> keySet = VALID_PROPERTY_KEYS.get(name);
        return new ArrayList<>(keySet);
    }

    /**
     * Gets the value of a property with the given key.
     * 
     * @param key the key of the property
     * @return the property value
     * @throws IllegalArgumentException if this {@link ContentProvider} has no valid property keys, or if there exists
     * no property with the given name
     */
    @Transient
    public String getProperty(final ContentProviderPropertyKey key) {
        List<ContentProviderPropertyKey> validPropertyKeys = getValidPropertyKeys();
        Validate.notNull(validPropertyKeys, "Valid property keys can not be null");
        Validate.isTrue(validPropertyKeys.contains(key), "Invalid property key: " + key + " for provider: " + name);
        return getProperties().get(key);
    }

    /**
     * Returns the mapping between the formats at this {@link ContentProvider} and their types.
     * 
     * @return a {@link Map} where the key is the unique format ID at this {@link ContentProvider} and the value is a
     * {@link FormatDecoration}
     */
    @OneToMany(mappedBy = "contentProvider", fetch = FetchType.LAZY)
    @MapKey(name = "contentProviderFormatId")
    public Map<String, FormatDecoration> getFormatDecorations() {
        return formatDecorations;
    }

    /**
     * Sets the mapping between the formats at this {@link ContentProvider} and their decorations.
     * 
     * @param formatDecorations the map between the formats at this {@link ContentProvider} and their format decorations
     * to set
     */
    public void setFormatDecorations(Map<String, FormatDecoration> formatDecorations) {
        this.formatDecorations = formatDecorations;
    }

    /**
     * Returns a list of format IDs.
     * 
     * @param locale
     * @return a list of format IDs
     */
    @Transient
    public final List<String> getFormatIds(final Locale locale) {
        final Set<String> formatIdSet = getFormatDecorations().keySet();
        final List<String> formatIdList = new ArrayList<>(formatIdSet);
        final Collator collator = Collator.getInstance(locale);
        final Comparator<String> comparator = Strings.getNaturalComparator(collator);
        Collections.sort(formatIdList, comparator);
        return formatIdList;
    }

    /**
     * Gets a specific {@link FormatDecoration} for the given format ID.
     * 
     * @param formatId the non-null ID of the format at this {@link ContentProvider}
     * @return a {@link FormatDecoration}
     * @throws NotFoundException
     */
    @Transient
    public FormatDecoration getFormatDecoration(String formatId) {
        final FormatDecoration formatDecoration = getFormatDecorations().get(formatId);

        if (formatDecoration == null) {
            final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.FORMAT_ID, formatId);
            final ErrorCauseArgument argument2 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, name);
            throw new NotFoundException(ErrorCause.FORMAT_NOT_FOUND, argument1, argument2);
        } else {
            return formatDecoration;
        }
    }

    /**
     * @see org.springframework.data.jpa.domain.AbstractPersistable#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ContentProvider)) {
            return false;
        }
        ContentProvider rhs = (ContentProvider) obj;
        return new EqualsBuilder().append(name, rhs.getName()).isEquals();
    }

    /**
     * @see org.springframework.data.jpa.domain.AbstractPersistable#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(name).toHashCode();
    }

    /**
     * Enumeration for content provider property keys.
     */
    public static enum ContentProviderPropertyKey {
        PRODUCT_URL, CREATE_LOAN_URL, ORDER_LIST_URL, CONSUME_LICENSE_URL, LOAN_EXPIRATION_DAYS
    }
}
