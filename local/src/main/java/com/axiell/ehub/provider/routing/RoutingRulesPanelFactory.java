package com.axiell.ehub.provider.routing;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

public class RoutingRulesPanelFactory implements IBreadCrumbPanelFactory  {

    @Override
    public BreadCrumbPanel create(final String componentId, final IBreadCrumbModel breadCrumbModel) {
        return new RoutingRulesPanel(componentId, breadCrumbModel);
    }
}
