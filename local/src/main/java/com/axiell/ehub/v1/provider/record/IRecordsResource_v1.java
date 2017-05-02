/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider.record;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_XML)
public interface IRecordsResource_v1 {

    @GET
    @Path("{recordId}/formats")
    Formats_v1 getFormats(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("recordId") String recordId, @QueryParam("language") String language);
}
