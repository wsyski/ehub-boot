package com.axiell.ehub.provider.routing;

import org.apache.wicket.markup.html.panel.Panel;

class RoutingRuleCreatePanel extends Panel {

    RoutingRuleCreatePanel(final String id, final RoutingRulesMediator mediator) {
        super(id);
        final RoutingRuleCreateForm createForm = new RoutingRuleCreateForm("createForm", mediator);
        add(createForm);
    }
}
