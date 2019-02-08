package com.axiell.ehub.v2.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.v2.provider.record.IRecordsResource_v2;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IContentProvidersResource_v2 {

    @GET
    ContentProvidersDTO_v2 root();

    @GET
    @Path("{alias}")
    ContentProviderDTO_v2 getContentProvider(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("alias") String contentProviderAlias);

    @Path("/{alias}/records")
    IRecordsResource_v2 records(@PathParam("alias") String contentProviderAlias);
}
