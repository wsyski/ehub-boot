package com.axiell.ehub.common.controller.external.v5_0.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.ContentProviderDTO;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.ContentProvidersDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

@Tag(name = "content-providers", description = "Content Providers API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Service
public interface IContentProvidersResource {

    @GET
    @Operation(
            summary = "Get content providers",
            description = "Get content providers"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful search operation"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not found: Resource not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error: Something went wrong on the server")
    })
    ContentProvidersDTO root();

    @GET
    @Path("{alias}")
    @Operation(
            summary = "Get content provider",
            description = "Get content provider by alias"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful search operation"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not found: Resource not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error: Something went wrong on the server")
    })
    ContentProviderDTO getContentProvider(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("alias") String contentProviderAlias);

    @Path("{alias}/records")
    IRecordsResource getRecordsResource(@PathParam("alias") String contentProviderAlias);
}
