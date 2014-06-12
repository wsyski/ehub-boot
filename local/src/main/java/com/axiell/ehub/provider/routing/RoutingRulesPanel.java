package com.axiell.ehub.provider.routing;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

class RoutingRulesPanel extends BreadCrumbPanel {
    private final RoutingRulesMediator mediator;

    RoutingRulesPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        setOutputMarkupId(true);
        mediator = new RoutingRulesMediator();
        mediator.registerRoutingRulesPanel(this);
        addRoutingRuleModalWindowPanel();
        addOrReplaceRoutingRulesListView();
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }

    @Override
    public void onActivate(final IBreadCrumbParticipant previous) {
        addOrReplaceRoutingRulesListView();
        super.onActivate(previous);
    }

    private void addRoutingRuleModalWindowPanel() {
        final RoutingRuleModalWindowPanel routingRuleModalWindowPanel = new RoutingRuleModalWindowPanel("routingRuleModalWindow", mediator);
        add(routingRuleModalWindowPanel);
    }

    private void addOrReplaceRoutingRulesListView() {
        final RoutingRulesListView routingRulesListView = new RoutingRulesListView("routingRules", mediator);
        addOrReplace(routingRulesListView);
    }
}
