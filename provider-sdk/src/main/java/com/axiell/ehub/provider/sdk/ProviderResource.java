package com.axiell.ehub.provider.sdk;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

public class ProviderResource implements IProviderResource {

    @Override
    public Response root() {
        throw new NotImplementedException("The root path has not been implemented yet");
    }

    @Override
    public CheckoutDTO checkout(@HeaderParam("Authorization") AuthInfo authInfo, CheckoutRequestDTO checkoutRequest) {
        return null;
    }

    @Override
    public CheckoutDTO getCheckout(@HeaderParam("Authorization") AuthInfo authInfo, Long ehubCheckoutId) {
        return null;
    }

    @Override
    public FormatsDTO records(String recordId) {
        return null;
    }
}
