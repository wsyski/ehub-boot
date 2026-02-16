/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common;

import com.axiell.ehub.common.util.TimestampAwareEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Abstract eHub base class for persistent entities. Stores the audition values in persistent fields.
 *
 * @param <P> the type of the auditing type's identifier
 */
@MappedSuperclass
@EntityListeners({TimestampAwareEntityListener.class})
public abstract class AbstractTimestampAwarePersistable<P extends Serializable> extends AbstractPersistable<P> implements ITimestampAware<P> {

    @Column(name = "CREATE_DATETIME", nullable = false, updatable = false)
    private Date createdDate;

    @Column(name = "MODIFY_DATETIME", nullable = false)
    private Date modifiedDate;

    /*
     * Gets the created date
     */
    public Instant getCreatedDate() {
        return createdDate == null ? null : createdDate.toInstant();
    }

    /**
     * Sets the created date
     *
     * @param createdDate created date.
     */
    public void setCreatedDate(final Instant createdDate) {
        this.createdDate = createdDate == null ? null : Date.from(createdDate);
    }

    /*
     * Gets the modified date.
     */
    public Instant getModifiedDate() {
        return modifiedDate == null ? null : modifiedDate.toInstant();
    }

    /**
     * Sets the created date
     *
     * @param modifiedDate modified date.
     */
    public void setModifiedDate(final Instant modifiedDate) {
        this.modifiedDate = modifiedDate == null ? null : Date.from(modifiedDate);
    }
}
