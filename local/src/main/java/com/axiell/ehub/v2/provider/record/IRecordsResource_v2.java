/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v2.provider.record;

import com.axiell.ehub.provider.record.RecordDTO;
import com.axiell.ehub.provider.record.RecordsDTO;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IRecordsResource_v2 {

    @GET
    RecordsDTO_v2 root();

    @GET
    @Path("{id}")
    RecordDTO_v2 getRecord(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("id") String contentProviderRecordId,
                        @DefaultValue("en") @QueryParam("language") String language);
}
