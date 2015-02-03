/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface IRecordsResource {

    @GET
    RecordsDTO root();

    @GET
    @Path("{id}")
    RecordDTO getRecord(@PathParam("id") String contentProviderRecordId, @DefaultValue("en") @QueryParam("language") String language);
}
