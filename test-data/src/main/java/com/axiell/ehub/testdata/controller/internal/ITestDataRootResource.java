package com.axiell.ehub.testdata.controller.internal;

import com.axiell.ehub.testdata.controller.internal.dto.TestDataDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Service
public interface ITestDataRootResource {

    @POST
    @Path("data")
    TestDataDTO init(@DefaultValue("TEST_EP") @QueryParam("contentProviderName") String contentProviderName,
                     @DefaultValue("false") @QueryParam("isLoanPerProduct") boolean isLoanPerProduct);

    @DELETE
    @Path("data")
    Response delete();
}
