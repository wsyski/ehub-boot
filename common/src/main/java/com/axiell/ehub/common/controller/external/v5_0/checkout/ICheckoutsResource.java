package com.axiell.ehub.common.controller.external.v5_0.checkout;

import com.axiell.ehub.common.FieldsDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.SearchResultDTO;
import com.axiell.authinfo.AuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

@Tag(name = "checkouts", description = "Checkouts API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Service
public interface ICheckoutsResource {

    @GET
    @Operation(
            summary = "Search checkout",
            description = "Search checkout"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful checkout testdata"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not found: Resource not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error: Something went wrong on the server")
    })
    SearchResultDTO<CheckoutMetadataDTO> search(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @QueryParam("lmsLoanId") final String lmsLoanId, @DefaultValue("en") @QueryParam("language") String language);

    @POST
    @Operation(
            summary = "Checkout",
            description = "Checkout"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful checkout testdata"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not found: Resource not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error: Something went wrong on the server")
    })
    CheckoutDTO checkout(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, FieldsDTO fields, @DefaultValue("en") @QueryParam("language") String language);

    @GET
    @Operation(
            summary = "Get checkout",
            description = "Get checkout"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful checkout testdata"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not found: Resource not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error: Something went wrong on the server")
    })
    @Path("{id}")
    CheckoutDTO getCheckout(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("id") Long ehubCheckoutId, @DefaultValue("en") @QueryParam("language") String language);
}
