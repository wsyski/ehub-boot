package com.axiell.ehub.provider.alias;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

class AliasField extends RequiredTextField<String> {

    AliasField(final String id, final AliasMapping aliasMapping) {
        super(id, new PropertyModel<String>(aliasMapping, "alias.value"));
        addLengthValidator();
        addAliasExistsValidator();
    }

    private void addLengthValidator() {
        final AliasLengthValidator validator = new AliasLengthValidator();
        add(validator);
    }

    private void addAliasExistsValidator() {
        final AliasExistsValidator validator = new AliasExistsValidator();
        add(validator);
    }
}
