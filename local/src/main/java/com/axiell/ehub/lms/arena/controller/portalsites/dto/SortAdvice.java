package com.axiell.ehub.lms.arena.controller.portalsites.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class SortAdvice implements Serializable {
    private static final long serialVersionUID = -4751503355698968815L;

    public static final String Relevance = "Relevance";
    public static final SortAdvice Default = new SortAdvice(Relevance, Direction.Descending);

    public final String field;
    public final Direction direction;

    public static SortAdvice field(String field) {
        return new SortAdvice(field, Default.direction);
    }

    public SortAdvice direction(Direction direction) {
        return new SortAdvice(this.field, direction);
    }

    private SortAdvice(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(direction).append(field).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SortAdvice)) {
            return false;
        }
        SortAdvice rhs = (SortAdvice) obj;
        return new EqualsBuilder().append(direction, rhs.getDirection()).append(field, rhs.getField()).isEquals();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static enum Direction {
        Ascending(true), Descending(false);

        private final boolean isAscending;

        private Direction(boolean isAscending) {
            this.isAscending = isAscending;
        }

        public boolean isAscending() {
	    return isAscending;
	}
    }

}
