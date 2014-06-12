package com.axiell.ehub.provider.routing;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

class SourceExistsValidator extends AbstractValidator<String> {
    @SpringBean(name = "routingAdminController")
    private IRoutingAdminController routingAdminController;

    SourceExistsValidator() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected void onValidate(final IValidatable<String> validatable) {
        final String sourceValue = validatable.getValue();
        final boolean exists = routingAdminController.existsRoutingRuleWith(sourceValue);

        if (exists)
            error(validatable, resourceKey());
    }

    @Override
    protected String resourceKey() {
        return "msgSourceAlreadyExists";
    }
}
