/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider;

import com.axiell.ehub.v1.provider.record.IRecordsResource_v1;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines the getContent provider related resources that are accessible through the eHUB REST interface.
 * 
 * <p>
 * <b>NOTE:</b> Due to a bug in the RESTEasy client framework the root path is not included in the final path when using
 * sub-resources, therefore the root path of this resource can unfortunately not be anything else than <code>/</code>,
 * but all paths on method level in this class <b>must</b> start with <b><code>v1/getContent-providers</code></b>.
 * </p>
 */
@Path("/")
@Produces(MediaType.APPLICATION_XML)
public interface IContentProvidersResource_v1 {
    /**
     * The root path of this resource.
     */
    String ROOT_PATH = "v1/content-providers";

    /**
     * Gets the {@link com.axiell.ehub.provider.record.IRecordsResource} for a specific {@link com.axiell.ehub.provider.ContentProviderName}.
     *
     * <p>
     * Example path: <code>/getContent-providers/elib/records</code>
     * </p>
     *
     * @param contentProviderName the name of the {@link com.axiell.ehub.provider.ContentProvider}
     * @return an {@link com.axiell.ehub.provider.record.IRecordsResource}
     * @throws
     */
    @Path(ROOT_PATH + "/{providerName}/records")
    IRecordsResource_v1 getRecords(@PathParam("providerName") String contentProviderName);
}
