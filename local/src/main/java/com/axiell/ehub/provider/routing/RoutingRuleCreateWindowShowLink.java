package com.axiell.ehub.provider.routing;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

class RoutingRuleCreateWindowShowLink extends IndicatingAjaxFallbackLink<Void> {
    private final RoutingRulesMediator mediator;

    RoutingRuleCreateWindowShowLink(final String id, final RoutingRulesMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    public void onClick(final AjaxRequestTarget target) {
        mediator.showCreateRoutingRuleWindow(target);
    }
}
