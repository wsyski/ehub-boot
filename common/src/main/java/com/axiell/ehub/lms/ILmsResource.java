/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.axiell.ehub.lms.record.ExportRecord;
import com.axiell.ehub.lms.record.ExportRecords;
import com.axiell.ehub.lms.record.IndexRecords;
import com.axiell.ehub.provider.ContentProvider;

/**
 * Defines the LMS related resources that are accessible through the eHUB REST interface.
 */
@Path("v1/lms")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public interface ILmsResource {

    /**
     * Parses the provided {@link ExportRecord}s (wrapped in an {@link ExportRecords} instance) to identify potential
     * {@link ContentProvider} records.
     * 
     * <p>
     * Example path: <code>v1/lms/records</code>
     * </p>
     * 
     * @param exportRecords the {@link ExportRecord}s to be parsed
     * @return an instance of {@link IndexRecords}
     */
    @POST
    @Path("records")
    IndexRecords parseExportRecords(ExportRecords exportRecords);
}
