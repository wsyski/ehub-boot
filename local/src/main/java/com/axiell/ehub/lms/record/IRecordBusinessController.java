/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.record;

import com.axiell.ehub.lms.record.ExportRecords;
import com.axiell.ehub.provider.ContentProvider;

/**
 * Defines all business methods related to {@link ExportRecords} and {@link IndexRecords} objects.
 */
public interface IRecordBusinessController {

    /**
     * Parses the provided {@link ExportRecord}s (wrapped in an {@link ExportRecords} instance) to identify potential
     * {@link ContentProvider} records.
     * 
     * <p>
     * The number of the returned {@link IndexRecord}s (wrapped in an {@link IndexRecords} instance) should be exactly
     * the same as the number of provided {@link ExportRecord}s (wrapped in an {@link ExportRecords} instance), even a
     * provided {@link ExportRecord} could not be identified as {@link ContentProvider} record.
     * </p>
     * 
     * @param exportRecords the {@link ExportRecord}s to be parsed
     * @return an instance of {@link IndexRecords}
     */
    IndexRecords parseExportRecords(ExportRecords exportRecords);
}
