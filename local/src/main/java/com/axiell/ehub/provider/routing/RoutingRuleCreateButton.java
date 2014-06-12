package com.axiell.ehub.provider.routing;

import com.axiell.ehub.provider.ContentProviderName;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

class RoutingRuleCreateButton extends AjaxButton {
    private final RoutingRulesMediator mediator;

    @SpringBean(name = "routingAdminController")
    private IRoutingAdminController routingAdminController;

    RoutingRuleCreateButton(final String id, final RoutingRulesMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        final RoutingRule routingRule = (RoutingRule) form.getModelObject();
        routingAdminController.save(routingRule);
        mediator.afterRoutingRuleWasCreated(target);
    }

    @Override
    protected void onError(final AjaxRequestTarget target, final Form<?> form) {
        mediator.onRoutingRuleCreateError(target);
    }
}
