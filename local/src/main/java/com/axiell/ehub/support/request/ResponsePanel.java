package com.axiell.ehub.support.request;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

class ResponsePanel extends Panel {

    ResponsePanel(String id, final DefaultSupportResponse response) {
        super(id);
        setOutputMarkupId(true);

        final SupportRequest request = response.getRequest();

        addLabel("uri", request.getUri());
        addLabel("httpMethod", request.getHttpMethod());
        addLabel("authInfo", request.getAuthInfo());
        addLabel("status", response.getStatus());
        addLabel("requestBody", request.getBody());
        addLabel("responseBody", response.getBody());
    }

    private void addLabel(final String id, String value) {
        final Label label = new Label(id, value);
        label.setVisible(value != null);
        add(label);
    }
}
