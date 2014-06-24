package com.axiell.ehub.support.request;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

class ResponsePanel extends Panel {

    ResponsePanel(String id, final SupportResponse response) {
        super(id);
        setOutputMarkupId(true);

        final SupportRequest request = response.getRequest();

        addLabel("uri", request.getUri());
        addLabel("httpMethod", request.getHttpMethod());
        addLabel("authInfo", request.getAuthInfo());
        addLabel("status", response.getStatus());
        addField("unformattedResponseBody", response.getBody());
    }

    private void addLabel(final String id, String value) {
        final Label label = new Label(id, value);
        add(label);
    }

    private void addField(final String fieldId, final String value) {
        final AttributeModifier attributeModifier = new AttributeModifier("value", true, new Model<>(value));
        final Label hiddenField = new Label(fieldId);
        hiddenField.add(attributeModifier);
        add(hiddenField);
    }
}
