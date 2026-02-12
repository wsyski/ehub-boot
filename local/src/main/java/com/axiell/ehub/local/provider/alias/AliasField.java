package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.Alias;
import com.axiell.ehub.common.provider.alias.AliasMapping;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

class AliasField extends RequiredTextField<String> {

    AliasField(final String id, final AliasMapping aliasMapping) {
        super(id, new PropertyModel<String>(aliasMapping, "alias.value"));
        addLengthValidator();
        addAliasExistsValidator();
    }

    private void addLengthValidator() {
        final StringValidator validator = new StringValidator(1, Alias.LENGTH);
        add(validator);
    }

    private void addAliasExistsValidator() {
        final AliasExistsValidator validator = new AliasExistsValidator();
        add(validator);
    }
}
