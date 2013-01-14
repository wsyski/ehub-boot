/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Abstract eHub base class for persistent entities. Stores the audition values in persistent fields.
 *
 * @param <PK> the type of the auditing type's identifier
 */
@MappedSuperclass
public abstract class AbstractTimestampAwarePersistable<PK extends Serializable> extends AbstractPersistable<PK> implements ITimestampAware<PK> {
    private static final long serialVersionUID = 141481953116476081L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE", nullable = false)
    private Date modifiedDate;

    /*
     * Gets the created date
     */
    public DateTime getCreatedDate() {
        return createdDate==null ? null : new DateTime(createdDate);
    }

    /**
     * Sets the created date
     *
     * @param createdDate created date.
     */
    public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = createdDate == null ?  null :createdDate.toDate();
    }

    /*
     * Gets the modified date.
     */
    public DateTime getModifiedDate() {
        return modifiedDate==null ? null : new DateTime(modifiedDate);
    }

    /**
     * Sets the created date
     *
     * @param modifiedDate modified date.
     */
    public void setModifiedDate(final DateTime modifiedDate) {
        this.modifiedDate = modifiedDate == null ?  null : modifiedDate.toDate();
    }
}
