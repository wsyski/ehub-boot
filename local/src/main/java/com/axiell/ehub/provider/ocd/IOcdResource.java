package com.axiell.ehub.provider.ocd;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IOcdResource {

    @GET
    @Path("v1/libraries/{libraryId}/media")
    List<MediaDTO> getAllMedia(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId);

    @GET
    @Path("v1/rpc/libraries/{libraryId}/patrons/{email}")
    PatronDTO getPatronByEmail(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId,
                               @PathParam("email") String email);

    @GET
    @Path("v1/rpc/libraries/{libraryId}/patrons/{libraryCard}")
    PatronDTO getPatronByLibraryCard(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId,
                                     @PathParam("libraryCard") String libraryCard);

    @POST
    @Path("v1/libraries/{libraryId}/patrons")
    PatronDTO addPatron(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId, PatronDTO patronDTO);

    @GET
    @Path("v1/libraries/{libraryId}/patrons")
    List<PatronDTO> getAllPatrons(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId);

    @POST
    @Path("v1/libraries/{libraryId}/patrons/{patronId}/checkouts/{isbn}")
    CheckoutSummaryDTO checkout(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId,
                                @PathParam("patronId") String patronId, @PathParam("isbn") String contentProviderRecordId);

    @DELETE
    @Path("v1/libraries/{libraryId}/patrons/{patronId}/checkouts/{isbn}")
    void checkin(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId,
                 @PathParam("patronId") String patronId, @PathParam("isbn") String contentProviderRecordId);

    @GET
    @Path("v1/libraries/{libraryId}/patrons/{patronId}/checkouts")
    List<CheckoutDTO> getCheckouts(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId,
                                   @PathParam("patronId") String patronId);
}
