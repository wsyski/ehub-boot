package com.axiell.ehub.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.provider.alias.AliasMappings;
import com.axiell.ehub.provider.record.IRecordsResource;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IContentProvidersResource {

    @GET
    @Cache(maxAge=300, sMaxAge = 300)
    ContentProvidersDTO root();

    @GET
    @Path("/{alias}")
    ContentProviderDTO getContentProvider(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("alias") String contentProviderAlias);

    @Path("/{alias}/records")
    IRecordsResource records(@PathParam("alias") String contentProviderAlias);
}
