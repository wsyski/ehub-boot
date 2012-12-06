/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.record;

import com.axiell.ehub.lms.record.ExportRecords;

/**
 * Default implementation of the {@link IRecordBusinessController}.
 */
public class RecordBusinessController implements IRecordBusinessController {

    /**
     * @see com.axiell.ehub.lms.record.IRecordBusinessController#parseExportRecords(com.axiell.ehub.lms.record.ExportRecords)
     */
    @Override
    public IndexRecords parseExportRecords(final ExportRecords exportRecords) {
        final IndexRecords indexRecords = new IndexRecords();

        /*
         * TODO: Apply the parsing algorithms for all ContentProviders to all the provided external URLs to identify
         * potential ContentProvider records
         */
        
        return indexRecords;
    }
}
