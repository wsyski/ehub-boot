package com.axiell.ehub.provider.elib.library3;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;

import java.lang.reflect.Method;

public class ElibClientErrorInterceptor implements ClientErrorInterceptor, AcceptedByMethod {

    @Override
    public boolean accept(@SuppressWarnings("rawtypes") final Class type, final Method method) {
        final Package thisPkg = getClass().getPackage();
        final Package providedPkg = type.getPackage();
        return thisPkg.equals(providedPkg);
    }


    @Override
    public void handle(final ClientResponse<?> clientResponse) throws RuntimeException {
        String errorMessage = clientResponse.getEntity(String.class);
        System.out.print(errorMessage);
    }
}
