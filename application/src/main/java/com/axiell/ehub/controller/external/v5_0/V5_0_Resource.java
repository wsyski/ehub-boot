package com.axiell.ehub.controller.external.v5_0;

import com.axiell.ehub.controller.external.v5_0.checkout.CheckoutsResource;
import com.axiell.ehub.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.controller.external.v5_0.hello.HelloResource;
import com.axiell.ehub.controller.external.v5_0.hello.IHelloResource;
import com.axiell.ehub.controller.external.v5_0.provider.ContentProvidersResource;
import com.axiell.ehub.controller.external.v5_0.provider.IContentProvidersResource;
import jakarta.ws.rs.Path;

public class V5_0_Resource implements IV5_0_Resource {

    public IContentProvidersResource contentProviders() {
        return new ContentProvidersResource();
    }

    public ICheckoutsResource checkouts() {
        return new CheckoutsResource();
    }

    @Override
    public IHelloResource getHelloResource() {
        return new HelloResource();
    }
}
