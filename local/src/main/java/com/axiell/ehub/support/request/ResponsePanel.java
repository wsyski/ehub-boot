package com.axiell.ehub.support.request;

import com.axiell.authinfo.AuthInfo;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.ws.rs.ext.ParamConverter;

class ResponsePanel extends Panel {
    @SpringBean
    private ParamConverter<AuthInfo> authInfoConverter;

    ResponsePanel(String id, final DefaultSupportResponse response) {
        super(id);
        setOutputMarkupId(true);

        final SupportRequest request = response.getRequest();

        addLabel("uri", request.getUri());
        addLabel("httpMethod", request.getHttpMethod());
        addLabel("authInfo", authInfoConverter.toString(request.getAuthInfo()));
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
