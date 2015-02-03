/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider.record;

import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_XML)
public interface IRecordsResource {

    @GET
    @Path("{recordId}/formats")
    Formats_v1 getFormats(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("recordId") String recordId, @QueryParam("language") String language);
}
