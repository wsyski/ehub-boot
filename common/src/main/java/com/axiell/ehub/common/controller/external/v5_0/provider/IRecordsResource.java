/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common.controller.external.v5_0.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.RecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

@Tag(name = "records", description = "Content Provider Records API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Service
public interface IRecordsResource {



    @GET
    @Path("{id}")
    @Operation(
            summary = "Get record",
            description = "Get records by content record id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful search operation"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not found: Resource not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error: Something went wrong on the server")
    })
    RecordDTO getRecord(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("id") String contentProviderRecordId,
                        @DefaultValue("en") @QueryParam("language") String language);
}
