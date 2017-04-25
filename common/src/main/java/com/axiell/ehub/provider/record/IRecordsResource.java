/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import com.axiell.auth.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IRecordsResource {

    @GET
    RecordsDTO root();

    @GET
    @Path("{id}")
    RecordDTO getRecord(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("id") String contentProviderRecordId,
                        @DefaultValue("en") @QueryParam("language") String language);
}
