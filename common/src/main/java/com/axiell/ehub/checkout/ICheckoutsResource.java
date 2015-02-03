package com.axiell.ehub.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.search.SearchResultDTO;

import javax.ws.rs.*;

public interface ICheckoutsResource {

    @GET
    SearchResultDTO<CheckoutMetadataDTO> search(@QueryParam("lmsLoanId") final String lmsLoanId, @DefaultValue("en") @QueryParam("language") String language);

    @POST
    CheckoutDTO checkout(FieldsDTO fields, @DefaultValue("en") @QueryParam("language") String language);

    @GET
    @Path("{id}")
    CheckoutDTO getCheckout(@PathParam("id") Long ehubCheckoutId, @DefaultValue("en") @QueryParam("language") String language);
}
