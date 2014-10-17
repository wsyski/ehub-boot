package com.axiell.ehub.support.request;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

public class RequestsGeneratorForm extends Form<RequestArguments> {

    RequestsGeneratorForm(final String id) {
        super(id);
        setFormModel();
        addEhubConsumerChoice();
        addTextField("patronId", false);
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

    public void addContentProviderNameField() {
        addTextField("contentProviderName", true);
    }

    public void addContentProviderRecordIdField() {
        addTextField("contentProviderRecordId", true);
    }

    public void addLangaugeField() {
        addTextField("language", false);
    }

    public void addLmsRecordIdField() {
        addTextField("lmsRecordId", false);
    }

    public void addFormatIdField() {
        addTextField("formatId", false);
    }

    public void addLmsLoanIdField() {
        addTextField("lmsLoanId", false);
    }

    private void addTextField(final String id, final boolean required) {
        final TextField<String> field = new TextField<>(id);
        field.setRequired(required);
        add(field);
    }
}
