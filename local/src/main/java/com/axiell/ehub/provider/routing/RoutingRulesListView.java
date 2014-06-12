package com.axiell.ehub.provider.routing;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class RoutingRulesListView extends ListView<RoutingRule> {
    private final RoutingRulesMediator mediator;

    @SpringBean(name = "routingAdminController")
    private IRoutingAdminController routingAdminController;

    RoutingRulesListView(final String id, final RoutingRulesMediator mediator) {
        super(id);
        this.mediator = mediator;
        setRoutingRuleList();
    }

    @Override
    protected void populateItem(final ListItem<RoutingRule> item) {
        final RoutingRule routingRule = item.getModelObject();
        final Label source = new Label("source", routingRule.getSource().getValue());
        final Label target = new Label("target", routingRule.getTarget().name());
        final RoutingRuleDeleteLink deleteLink = new RoutingRuleDeleteLink("deleteLink", routingRule, mediator);
        item.add(deleteLink);
        item.add(source);
        item.add(target);
    }

    private void setRoutingRuleList() {
        final List<RoutingRule> routingRules = routingAdminController.getRoutingRules();
        setList(routingRules);
    }
}
