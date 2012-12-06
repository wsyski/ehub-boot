/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;

/**
 * Defines the content provider records related resources that are accessible through the eHUB REST interface.
 */
@Produces(MediaType.APPLICATION_XML)
public interface IRecordsResource {

    /**
     * Gets the {@link Formats} for a specific record at a certain {@link ContentProvider}.
     * 
     * <p>
     * Example path: <code>v1/content-providers/elib/records/123456/formats?language=en</code>
     * </p>
     * 
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID
     * @param recordId the ID of the record at the {@link ContentProvider}
     * @param language the ISO 639 alpha-2 or alpha-3 language code used when getting the translated names and
     * descriptions of the {@link Formats}, can be <code>null</code>
     * @return an instance of {@link Formats}
     */
    @GET
    @Path("{recordId}/formats")
    Formats getFormats(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("recordId") String recordId, @QueryParam("language") String language);
}
