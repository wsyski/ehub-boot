/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.record;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang3.Validate;

/**
 * Represents the records to be indexed in an LMS (or its clients).
 */
@XmlRootElement(name = "indexRecords")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(IndexRecord.class)
public final class IndexRecords {
    private Set<IndexRecord> records = new HashSet<>();

    /**
     * Returns a {@link Set} of {@link IndexRecord}s.
     * 
     * @return a {@link Set} of {@link IndexRecord}s
     */
    @XmlElement(name = "indexRecord", required = true)
    public Set<IndexRecord> getIndexRecords() {
        return records;
    }

    /**
     * Sets a {@link Set} of {@link IndexRecord}s.
     * 
     * @param records the {@link Set} of {@link IndexRecord}s to set
     * @throws NullPointerException if the provided {@link Set} is null
     */
    protected void setIndexRecords(final Set<IndexRecord> records) {
        Validate.notNull(records, "Can't add a Set of IndexRecord objects that is null");
        this.records = records;
    }

    /**
     * Adds an {@link IndexRecord} to the underlying {@link Set} of {@link IndexRecord}s.
     * 
     * @param record the {@link IndexRecord} to add
     * @throws NullPointerException if the provided {@link IndexRecord} is null
     */
    public void addIndexRecord(final IndexRecord record) {
        Validate.notNull(record, "Can't add a IndexRecord that is null");
        records.add(record);
    }

}
