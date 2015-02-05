/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface IContentProvidersResource {

    @GET
    ContentProvidersDTO root();

    @GET
    @Path("{alias}")
    ContentProviderDTO getContentProvider(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("alias") String contentProviderAlias);

    @Path("/{alias}/records")
    IRecordsResource records(@PathParam("alias") String contentProviderAlias);
}
