package com.axiell.ehub.support.request;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

class RequestsGeneratorForm extends Form<RequestArguments> {

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
        final RequestArguments arguments = new RequestArguments(this);
        final CompoundPropertyModel<RequestArguments> model = new CompoundPropertyModel<>(arguments);
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

    private void addTextField(final String id, final boolean required) {
        final TextField<String> field = new TextField<>(id);
        field.setRequired(required);
        add(field);
    }
}
