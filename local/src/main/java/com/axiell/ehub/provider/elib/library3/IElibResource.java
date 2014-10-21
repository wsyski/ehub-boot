package com.axiell.ehub.provider.elib.library3;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface IElibResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("books/availability/{productId}")
    BookAvailability getBookAvailability(@QueryParam("serviceid") String serviceId, @QueryParam("checksum") String checksum, @PathParam("productId") String elibProductId, @QueryParam("card") String patronId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("products/{productId}")
    GetProductResponse getProduct(@QueryParam("serviceid") String serviceId, @QueryParam("checksum") String checksum, @PathParam("productId") String elibProductId);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("loans")
    CreateLoanResponse createLoan(@FormParam("ServiceID") String serviceId,
                           @FormParam("Card") String patronId,
                           @FormParam("ProductID") String elibProductId,
                           @FormParam("Checksum") String checksum);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("loans/{loanId}")
    GetLoanResponse getLoan(@QueryParam("serviceid") String serviceId, @QueryParam("checksum") String checksum, @PathParam("loanId") String loanId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("loans")
    GetLoansResponse getLoans(@QueryParam("serviceid") String serviceId, @QueryParam("checksum") String checksum, @QueryParam("card") String patronId, @QueryParam("onlyactive") Boolean onlyActive);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("libraryproducts/{productId}")
    LibraryProductResponse getLibraryProduct(@QueryParam("serviceid") String serviceId, @QueryParam("checksum") String checksum, @PathParam("productId") String elibProductId);
}
