/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.*;

public interface IRecordsResource {

    @GET
    RecordsDTO root();

    @GET
    @Path("{id}")
    RecordDTO getRecord(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("id") String contentProviderRecordId,
                        @DefaultValue("en") @QueryParam("language") String language);
}
