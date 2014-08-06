package com.axiell.ehub.support.about;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DATABASECHANGELOG")
@Access(AccessType.FIELD)
public class DatabaseChangeLog {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "FILENAME", nullable = false)
    private String fileName;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATEEXECUTED", nullable = false)
    private Date dateExecuted;

    @Column(name = "ORDEREXECUTED", nullable = false)
    private Integer orderExecuted;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "TAG")
    private String tag;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getFileName() {
        return fileName;
    }

    public DateTime getDateExecuted() {
        return dateExecuted == null ? null : new DateTime(dateExecuted);
    }

    public Integer getOrderExecuted() {
        return orderExecuted;
    }

    public void setOrderExecuted(Integer orderExecuted) {
        this.orderExecuted = orderExecuted;
    }

    public String getComments() {
        return comments;
    }

    public String getTag() {
        return tag;
    }
}
