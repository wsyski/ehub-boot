/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Response;

import com.axiell.ehub.provider.record.IRecordsResource;

public interface IContentProvidersResource {

    @GET
    ContentProvidersDTO root();

    @GET
    @Path("{alias}")
    ContentProviderDTO getContentProvider(@PathParam("alias") String contentProviderAlias);

    @Path("/{alias}/records")
    IRecordsResource records(@PathParam("alias") String contentProviderAlias);
}
