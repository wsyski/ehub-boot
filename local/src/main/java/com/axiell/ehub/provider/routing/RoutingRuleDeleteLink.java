package com.axiell.ehub.provider.routing;

import com.axiell.ehub.ConfirmationLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

class RoutingRuleDeleteLink extends ConfirmationLink<RoutingRule> {
    private final RoutingRulesMediator mediator;

    @SpringBean(name = "routingAdminController")
    private IRoutingAdminController routingAdminController;

    RoutingRuleDeleteLink(final String id, final RoutingRule routingRule, final RoutingRulesMediator mediator) {
        super(id);
        this.mediator = mediator;
        setModel(routingRule);
    }

    @Override
    public void onClick() {
        final RoutingRule routingRule = getModelObject();
        routingAdminController.delete(routingRule);
        mediator.afterRoutingRuleWasDeleted();
    }

    private void setModel(RoutingRule routingRule) {
        setModel(new Model<RoutingRule>(routingRule));
    }
}
