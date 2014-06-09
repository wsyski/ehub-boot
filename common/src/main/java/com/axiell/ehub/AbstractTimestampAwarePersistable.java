/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.util.TimestampAwareEntityListener;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Abstract eHub base class for persistent entities. Stores the audition values in persistent fields.
 *
 * @param <PK> the type of the auditing type's identifier
 */
@MappedSuperclass
@EntityListeners({TimestampAwareEntityListener.class})
public abstract class AbstractTimestampAwarePersistable<PK extends Serializable> extends AbstractPersistable<PK> implements ITimestampAware<PK> {
    private static final long serialVersionUID = 141481953116476081L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATETIME", nullable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_DATETIME", nullable = false)
    private Date modifiedDate;

    /*
     * Gets the created date
     */
    public DateTime getCreatedDate() {
        return createdDate == null ? null : new DateTime(createdDate);
    }

    /**
     * Sets the created date
     *
     * @param createdDate created date.
     */
    public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = createdDate == null ? null : createdDate.toDate();
    }

    /*
     * Gets the modified date.
     */
    public DateTime getModifiedDate() {
        return modifiedDate == null ? null : new DateTime(modifiedDate);
    }

    /**
     * Sets the created date
     *
     * @param modifiedDate modified date.
     */
    public void setModifiedDate(final DateTime modifiedDate) {
        this.modifiedDate = modifiedDate == null ? null : modifiedDate.toDate();
    }
}
