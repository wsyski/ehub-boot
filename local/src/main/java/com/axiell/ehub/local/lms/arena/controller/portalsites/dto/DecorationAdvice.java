package com.axiell.ehub.local.lms.arena.controller.portalsites.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class DecorationAdvice implements Serializable {

    public static final DecorationAdvice Tags = new DecorationAdvice(Type.Tags);
    public static final DecorationAdvice Ratings = new DecorationAdvice(Type.Ratings);
    public static final DecorationAdvice Reviews = new DecorationAdvice(Type.Reviews);
    public static final DecorationAdvice RecordGrouping = new DecorationAdvice(Type.RecordGrouping);
    public static final DecorationAdvice Covers = new DecorationAdvice(Type.Covers);
    public static final DecorationAdvice Availability = new DecorationAdvice(Type.Availability);
    public static final DecorationAdvice Facets = new DecorationAdvice(Type.Facets);
    public final Type type;
    private String selector = null;
    private Integer limit = null;
    private List<SortAdvice> sort = null;

    /**
     * This constructor provides the possibility to deserialize instances of this class.
     */
    protected DecorationAdvice() {
        this(null, null, null, null);
    }

    private DecorationAdvice(final Type type) {
        this.type = type;
        switch (type) {
            case Reviews:
            case RecordGrouping:
            case Tags:
                this.limit = Integer.MAX_VALUE;
                break;
            default:
                break;
        }
    }

    private DecorationAdvice(Type type, String selector, Integer limit, List<SortAdvice> sort) {
        this.type = type;
        this.selector = selector;
        this.limit = limit;
        this.sort = sort;
    }

    public static DecorationAdvice valueOf(String s) {
        switch (Type.valueOf(s)) {
            case RecordGrouping:
                return RecordGrouping;
            case Reviews:
                return Reviews;
            case Tags:
                return Tags;
            case Ratings:
                return Ratings;
            case Covers:
                return Covers;
            case Availability:
                return Availability;
            case Facets:
                return Facets;
            default:
                throw new IllegalArgumentException("No enum const class " + DecorationAdvice.class.getName() + "." + s);
        }
    }

    public DecorationAdvice of(final String selector) {
        switch (this.type) {
            case RecordGrouping:
            case Facets:
                return new DecorationAdvice(this.type, selector, this.limit, this.sort);
            default:
                throw new IllegalArgumentException("Cannot set 'of' on this kind of decoration type: " + this.type.name());
        }
    }

    public DecorationAdvice limit(final Integer limit) {
        Validate.notNull(limit, "limit must not be null");
        switch (type) {
            case Reviews:
            case RecordGrouping:
            case Facets:
            case Tags:
                return limit.equals(this.limit) ? this : new DecorationAdvice(this.type, this.selector, limit, this.sort);
            default:
                throw new IllegalArgumentException("Cannot set 'limit' on this kind of decoration type: " + this.type.name());
        }
    }

    public DecorationAdvice sort(final List<SortAdvice> sort) {
        switch (this.type) {
            case RecordGrouping:
                return new DecorationAdvice(this.type, this.selector, this.limit, sort);
            default:
                throw new IllegalArgumentException("Cannot set 'sort' on this kind of decoration type: " + this.type.name());
        }
    }

    public boolean isValid() {
        switch (type) {
            case RecordGrouping:
            case Facets:
                if (StringUtils.isBlank(selector)) return false;
                if (limit == null || limit < 0) return false;
                break;
            case Reviews:
            case Tags:
                if (limit == null || limit < 0) return false;
                break;
            default:
                break;
        }
        return true;
    }

    public String selector() {
        return selector;
    }

    public List<SortAdvice> sort() {
        return sort;
    }

    public Integer limit() {
        return limit;
    }

    public String name() {
        return this.type.name();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DecorationAdvice)) {
            return false;
        }
        DecorationAdvice rhs = (DecorationAdvice) obj;
        return new EqualsBuilder().append(type, rhs.type).append(selector(), rhs.selector()).append(limit(), rhs.limit()).append(sort(), rhs.sort()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(type).append(selector()).append(limit()).append(sort()).toHashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public enum Type {
        Tags, Ratings, Covers, Availability, Facets, RecordGrouping, Reviews
    }
}
