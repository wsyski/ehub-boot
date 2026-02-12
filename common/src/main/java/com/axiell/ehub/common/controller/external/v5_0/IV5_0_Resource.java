package com.axiell.ehub.common.controller.external.v5_0;

import com.axiell.ehub.common.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.common.controller.external.v5_0.provider.IContentProvidersResource;
import jakarta.ws.rs.Path;
import org.springframework.stereotype.Service;

@Service
public interface IV5_0_Resource {

    @Path("content-providers")
    IContentProvidersResource contentProviders();

    @Path("checkouts")
    ICheckoutsResource checkouts();

}
