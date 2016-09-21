package com.axiell.ehub.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ICheckoutsResource {

    @GET
    SearchResultDTO<CheckoutMetadataDTO> search(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @QueryParam("lmsLoanId") final String lmsLoanId, @DefaultValue("en") @QueryParam("language") String language);

    @POST
    CheckoutDTO checkout(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, FieldsDTO fields, @DefaultValue("en") @QueryParam("language") String language);

    @GET
    @Path("{id}")
    CheckoutDTO getCheckout(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("id") Long ehubCheckoutId, @DefaultValue("en") @QueryParam("language") String language);
}
