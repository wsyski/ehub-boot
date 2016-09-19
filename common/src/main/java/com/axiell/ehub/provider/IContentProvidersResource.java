/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IContentProvidersResource {

    @GET
    ContentProvidersDTO root();

    @GET
    @Path("{alias}")
    ContentProviderDTO getContentProvider(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("alias") String contentProviderAlias);

    @Path("/{alias}/records")
    IRecordsResource records(@PathParam("alias") String contentProviderAlias);
}
