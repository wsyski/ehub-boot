package com.axiell.ehub.version;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

@Entity
@Table(name = "RELEASE")
@Access(AccessType.FIELD)
public class Release implements Serializable {
    @Id
    @Column(name = "VERSION")
    private String version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_DATETIME", nullable = false)
    private Date modifiedDate;

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    @Override
    public String toString() {
	return version;
    }

    public DateTime getCreatedDate() {
	return createdDate == null ? null : new DateTime(createdDate);
    }

    public void setCreatedDate(final DateTime createdDate) {
	this.createdDate = createdDate == null ? null : createdDate.toDate();
    }

    public DateTime getModifiedDate() {
	return modifiedDate == null ? null : new DateTime(modifiedDate);
    }

    public void setModifiedDate(final DateTime modifiedDate) {
	this.modifiedDate = modifiedDate == null ? null : modifiedDate.toDate();
    }
}
