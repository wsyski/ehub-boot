/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms;

import org.springframework.beans.factory.annotation.Required;

import com.axiell.ehub.lms.record.ExportRecords;
import com.axiell.ehub.lms.record.IRecordBusinessController;
import com.axiell.ehub.lms.record.IndexRecords;

/**
 * Default implementation of the {@link ILmsResource}.
 */
public class LmsResource implements ILmsResource {
    private IRecordBusinessController recordBusinessController;

    /**
     * @see com.axiell.ehub.lms.ILmsResource#parseExportRecords(com.axiell.ehub.lms.record.ExportRecords)
     */
    @Override
    public IndexRecords parseExportRecords(final ExportRecords exportRecords) {
        return recordBusinessController.parseExportRecords(exportRecords);
    }

    /**
     * Sets the {@link IRecordBusinessController}.
     *
     * @param recordBusinessController the {@link IRecordBusinessController} to set
     */
    @Required
    public void setRecordBusinessController(final IRecordBusinessController recordBusinessController) {
        this.recordBusinessController = recordBusinessController;
    }
}
