package com.axiell.ehub.support.request;

import com.axiell.ehub.security.AuthInfo;

class SupportRequest {
    private AuthInfo authInfo;
    private String httpMethod;
    private String uri;
    private String body;

    String getAuthInfo() {
        return authInfo == null ? null : authInfo.toString();
    }

    void setAuthInfo(AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    String getHttpMethod() {
        return httpMethod;
    }

    void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    String getUri() {
        return uri;
    }

    void setUri(String uri) {
        this.uri = uri;
    }

    String getBody() {
        return body;
    }

    void setBody(String body) {
        this.body = body;
    }
}
