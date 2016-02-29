package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.checkout.CheckoutDTO;
import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ICheckoutsResource_v2 {

    @GET
    SearchResultDTO<CheckoutMetadataDTO> search(@HeaderParam("Authorization") AuthInfo authInfo, @QueryParam("lmsLoanId") final String lmsLoanId,
                                                @DefaultValue("en") @QueryParam("language") String language);

    @POST
    CheckoutDTO_v2 checkout(@HeaderParam("Authorization") AuthInfo authInfo, FieldsDTO fields, @DefaultValue("en") @QueryParam("language") String language);

    @GET
    @Path("{id}")
    CheckoutDTO_v2 getCheckout(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("id") Long ehubCheckoutId,
                            @DefaultValue("en") @QueryParam("language") String language);
}
