package com.axiell.ehub.common.provider;

import com.axiell.ehub.common.AbstractTimestampAwarePersistable;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.ErrorCauseArgument.Type;
import com.axiell.ehub.common.NotFoundException;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.util.HashCodeBuilderFactory;
import com.eekboom.utils.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;

import  jakarta.persistence.Access;
import  jakarta.persistence.AccessType;
import  jakarta.persistence.CollectionTable;
import  jakarta.persistence.Column;
import  jakarta.persistence.ElementCollection;
import  jakarta.persistence.Entity;
import  jakarta.persistence.EnumType;
import  jakarta.persistence.FetchType;
import  jakarta.persistence.ForeignKey;
import  jakarta.persistence.JoinColumn;
import  jakarta.persistence.MapKey;
import  jakarta.persistence.MapKeyColumn;
import  jakarta.persistence.MapKeyEnumerated;
import  jakarta.persistence.OneToMany;
import  jakarta.persistence.Table;
import  jakarta.persistence.Transient;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS;
import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.OAUTH_PATRON_URL;
import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.OAUTH_URL;
import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.PATRON_API_BASE_URL;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Represents a Content Provider. It contains the common parameters for a certain Content Provider, e.g. the base URL of
 * the Content Provider's web services.
 */
@Entity
@Table(name = "CONTENT_PROVIDER")
@Access(AccessType.PROPERTY)
public class ContentProvider extends AbstractTimestampAwarePersistable<Long> implements Serializable {
    public static final String CONTENT_PROVIDER_ELIB3 = "ELIB3";
    public static final String CONTENT_PROVIDER_OVERDRIVE = "OVERDRIVE";

    private static final Map<String, Set<ContentProviderPropertyKey>> VALID_PROPERTY_KEYS = ImmutableMap.<String, Set<ContentProviderPropertyKey>>builder()
            .put(CONTENT_PROVIDER_ELIB3, newHashSet(API_BASE_URL))
            .put(CONTENT_PROVIDER_OVERDRIVE, newHashSet(OAUTH_URL, OAUTH_PATRON_URL, API_BASE_URL, PATRON_API_BASE_URL))
            .build();
    private static final Set<ContentProviderPropertyKey> EP_VALID_PROPERTY_KEYS = newHashSet(API_BASE_URL);
    private static final Map<ContentProviderPropertyKey, Pattern> PROPERTY_PATTERNS = ImmutableMap.<ContentProviderPropertyKey, Pattern>builder()
            .put(LOAN_EXPIRATION_DAYS, Pattern.compile("[0-9]+")).build();

    private String name;
    private boolean isLoanPerProduct;
    private Map<ContentProviderPropertyKey, String> properties;
    private Map<String, FormatDecoration> formatDecorations;

    /**
     * Empty constructor required by JPA
     */
    public ContentProvider() {
    }

    /**
     * Constructs a new {@link ContentProvider}.
     *
     * @param name       the name of the {@link ContentProvider}
     * @param properties the {@link ContentProvider} properties
     */
    public ContentProvider(final String name, final Map<ContentProviderPropertyKey, String> properties) {
        this.name = name;
        this.properties = properties;
    }

    /**
     * Returns the name of the {@link ContentProvider}.
     *
     * @return the name of the {@link ContentProvider}
     */
    @Column(name = "NAME", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the {@link ContentProvider}. Only used by JPA.
     *
     * @param name the name of the {@link ContentProvider} to set
     */
    public void setName(final String name) {
        this.name = name;
    }


    @Column(name = "LOAN_PER_PRODUCT", nullable = false)
    public boolean isLoanPerProduct() {
        return isLoanPerProduct;
    }

    public void setLoanPerProduct(final boolean isLoanPerProduct) {
        this.isLoanPerProduct = isLoanPerProduct;
    }

    /**
     * Gets the {@link ContentProvider} properties.
     *
     * @return the {@link ContentProvider} properties
     */
    @ElementCollection
    @CollectionTable(name = "CONTENT_PROVIDER_PROPERTY", joinColumns = @JoinColumn(name = "CONTENT_PROVIDER_ID"), foreignKey = @ForeignKey(name = "FK_CONTENT_P_P_CONTENT_P"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "PROPERTY_KEY", nullable = false)
    @Column(name = "PROPERTY_VALUE")
     public Map<ContentProviderPropertyKey, String> getProperties() {
        return properties;
    }

    /**
     * Sets the {@link ContentProvider} properties. Only used by JPA.
     *
     * @param properties the {@link ContentProvider} properties to set
     */
    public void setProperties(Map<ContentProviderPropertyKey, String> properties) {
        this.properties = properties;
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
     *                          to set
     */
    public void setFormatDecorations(Map<String, FormatDecoration> formatDecorations) {
        this.formatDecorations = formatDecorations;
    }

    /**
     * Gets the valid properties for this {@link ContentProvider}.
     *
     * @return a {@link List} of {@link ContentProviderPropertyKey}s
     */
    @Transient
    public List<ContentProviderPropertyKey> getValidPropertyKeys() {
        Set<ContentProviderPropertyKey> keys = name == null ? EP_VALID_PROPERTY_KEYS : VALID_PROPERTY_KEYS.get(name);
        return new ArrayList<>(keys == null ? EP_VALID_PROPERTY_KEYS : keys);
    }

    /**
     * Gets the value of a property with the given key.
     *
     * @param key the key of the property
     * @return the property value
     * @throws IllegalArgumentException if this {@link ContentProvider} has no valid property keys, or if there exists
     *                                  no property with the given name
     */
    @Transient
    public String getProperty(final ContentProviderPropertyKey key) {
        List<ContentProviderPropertyKey> validPropertyKeys = getValidPropertyKeys();
        Validate.notNull(validPropertyKeys, "Valid property keys can not be null");
        Validate.isTrue(validPropertyKeys.contains(key), "Invalid property key: " + key + " for provider: " + name);
        return getProperties().get(key);
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

    @Transient
    public boolean isEhubProvider() {
        return !CONTENT_PROVIDER_ELIB3.equals(name) &&
                !CONTENT_PROVIDER_OVERDRIVE.equals(name);
    }

    @Transient
    public Pattern getPropertyValidatorPattern(final ContentProviderPropertyKey contentProviderPropertyKey) {
        return PROPERTY_PATTERNS.get(contentProviderPropertyKey);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ContentProvider)) {
            return false;
        }
        ContentProvider rhs = (ContentProvider) obj;
        return new EqualsBuilder().append(name, rhs.getName()).isEquals();
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilderFactory.create().append(name).toHashCode();
    }

    /**
     * Enumeration for getContent provider property keys.
     */
    public static enum ContentProviderPropertyKey {
        PRODUCT_URL, CONSUME_LICENSE_URL, LOAN_EXPIRATION_DAYS, OAUTH_URL, API_BASE_URL, OAUTH_PATRON_URL, PATRON_API_BASE_URL
    }
}
