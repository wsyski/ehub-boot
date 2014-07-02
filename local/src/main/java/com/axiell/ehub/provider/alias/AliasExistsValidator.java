package com.axiell.ehub.provider.alias;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

class AliasExistsValidator extends AbstractValidator<String> {
    @SpringBean(name = "aliasAdminController")
    private IAliasAdminController aliasAdminController;

    AliasExistsValidator() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected void onValidate(final IValidatable<String> validatable) {
        final String aliasValue = validatable.getValue();
        final boolean exists = aliasAdminController.existsAlias(aliasValue);

        if (exists)
            error(validatable, resourceKey());
    }

    @Override
    protected String resourceKey() {
        return "msgAliasAlreadyExists";
    }
}
