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
 * Represents exported records from the LMS.
 */
@XmlRootElement(name = "exportRecords")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(ExportRecord.class)
public final class ExportRecords {
    private Set<ExportRecord> records = new HashSet<>();

    /**
     * Returns a {@link Set} of {@link ExportRecord}s.
     * 
     * @return a {@link Set} of {@link ExportRecord}s
     */
    @XmlElement(name = "exportRecord", required = true)
    public Set<ExportRecord> getExportRecords() {
        return records;
    }

    /**
     * Sets a {@link Set} of {@link ExportRecord}s.
     * 
     * @param records the {@link Set} of {@link ExportRecord}s to set
     * @throws NullPointerException if the provided {@link Set} is null
     */
    protected void setExportRecords(Set<ExportRecord> records) {
        Validate.notNull(records, "Can't add a Set of ExportRecord objects that is null");
        this.records = records;
    }

    /**
     * Adds a {@link ExportRecord} to the underlying {@link Set} of {@link ExportRecord}s.
     * 
     * @param record the {@link ExportRecord} to add
     * @throws NullPointerException if the provided {@link ExportRecord} is null
     */
    public void addExportRecord(ExportRecord record) {
        Validate.notNull(record, "Can't add an ExportRecord that is null");
        records.add(record);
    }
}
