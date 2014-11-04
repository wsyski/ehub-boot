package com.axiell.ehub.provider;

import org.jboss.resteasy.client.ClientResponseFailure;

import javax.ws.rs.core.Response;

public class ResponseAnalyser {
    private final int status;

    private ResponseAnalyser(Response response) {
        status = response.getStatus();
    }

    public boolean isUnauthorized() {
        return Response.Status.UNAUTHORIZED.getStatusCode() == status;
    }

    public static ResponseAnalyser from(ClientResponseFailure crf) {
        final Response response = crf.getResponse();
        return new ResponseAnalyser(response);
    }
}
