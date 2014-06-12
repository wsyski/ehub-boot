package com.axiell.ehub.provider.routing;

import org.apache.wicket.markup.html.panel.Panel;

class RoutingRuleModalWindowPanel extends Panel {

    RoutingRuleModalWindowPanel(final String id, final RoutingRulesMediator mediator) {
        super(id);
        addRoutingRuleCreateWindowShowLink(mediator);
        addRoutingRuleCreateModalWindow(mediator);
    }

    private void addRoutingRuleCreateWindowShowLink(RoutingRulesMediator mediator) {
        final RoutingRuleCreateWindowShowLink showLink = new RoutingRuleCreateWindowShowLink("showLink", mediator);
        add(showLink);
    }

    private void addRoutingRuleCreateModalWindow(RoutingRulesMediator mediator) {
        final RoutingRuleCreateModalWindow modalWindow = new RoutingRuleCreateModalWindow("window", mediator);
        mediator.registerCreateModalWindow(modalWindow);
        add(modalWindow);
    }
}
