package com.axiell.ehub.support.request;

public class DefaultSupportResponse implements ISupportResponse {
    private final SupportRequest request;
    private final String status;
    private final String body;

    public DefaultSupportResponse(final SupportRequest request, final String status, final String body) {
        this.request = request;
        this.status = status;
        this.body = body;
    }

    SupportRequest getRequest() {
        return request;
    }

    String getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
