package com.axiell.ehub.support;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

class ResponsePanel extends Panel {

    ResponsePanel(String id, final SupportResponse response) {
        super(id);
        setOutputMarkupId(true);

        final SupportRequest request = response.getRequest();

        Label uriLabel = new Label("uri", request.getUri());
        add(uriLabel);

        Label httpMethodLabel = new Label("httpMethod", request.getHttpMethod());
        add(httpMethodLabel);

        Label authInfoLabel = new Label("authInfo", request.getAuthInfo());
        add(authInfoLabel);

        Label statusLabel = new Label("status", response.getStatus());
        add(statusLabel);

        addField("xmlData", response.getBody());
    }

    private void addField(String fieldId, String value) {
        AttributeModifier attributeModifier = new AttributeModifier("value", true, new Model<>(value));
        Label hiddenField = new Label(fieldId);
        hiddenField.add(attributeModifier);
        add(hiddenField);
    }
}
