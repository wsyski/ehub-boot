package com.axiell.ehub.support.request;

class SupportResponse {
    private final SupportRequest request;
    private final String status;
    private final String body;

    SupportResponse(SupportRequest request, String status, String body) {
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

    String getBody() {
        return body;
    }
}
