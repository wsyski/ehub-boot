package com.axiell.ehub.support.request;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

class NonExecutableClientExecutor extends SupportClientExecutor {

    NonExecutableClientExecutor(final SupportRequest supportRequest) {
        super(supportRequest);
    }

    @Override
    protected ClientResponse customExecute(final ClientRequest clientRequest) throws Exception {
        throw new NotExecutedException();
    }
}
