package com.axiell.ehub.support;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebRequest;

import javax.servlet.http.HttpServletRequest;

class RequestsGeneratorForm extends Form<RequestParameters> {

    RequestsGeneratorForm(final String id) {
        super(id);
        setFormModel();
        addEhubConsumerChoice();
        addTextField("libraryCard", false);
        addTextField("pin", false);
    }

    private void addEhubConsumerChoice() {
        final EhubConsumerChoice ehubConsumerChoice = new EhubConsumerChoice("ehubConsumer");
        add(ehubConsumerChoice);
    }

    private void setFormModel() {
        final RequestParameters reqParams = new RequestParameters();
        reqParams.setBaseUri(getBaseUri());
        final CompoundPropertyModel<RequestParameters> model = new CompoundPropertyModel<>(reqParams);
        setModel(model);
    }

    protected void addContentProviderNameField() {
        addTextField("contentProviderName", true);
    }

    protected void addContentProviderRecordIdField() {
        addTextField("contentProviderRecordId", true);
    }

    protected void addLangaugeField() {
        addTextField("language", false);
    }

    private void addTextField(String id, final boolean required) {
        final TextField<String> field = new TextField<>(id);
        field.setRequired(required);
        add(field);
    }

    private String getBaseUri() {
        WebRequest webRequest = (WebRequest) getRequest();
        HttpServletRequest httpServletRequest = webRequest.getHttpServletRequest();
        int port = httpServletRequest.getServerPort();
        String serverName = httpServletRequest.getServerName();
        String scheme = httpServletRequest.getScheme();
        return scheme + "://" + serverName + ":" + port;
    }
}
