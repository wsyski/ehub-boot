package com.axiell.ehub.provider.routing;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

class SourceField extends RequiredTextField<String> {

    SourceField(final String id, final RoutingRule routingRule) {
        super(id, new PropertyModel<String>(routingRule, "source.value"));
        addSourceLengthValidator();
        addSourceExistsValidator();
    }

    private void addSourceLengthValidator() {
        final SourceLengthValidator validator = new SourceLengthValidator();
        add(validator);
    }

    private void addSourceExistsValidator() {
        final SourceExistsValidator validator = new SourceExistsValidator();
        add(validator);
    }
}
