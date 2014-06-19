package com.axiell.ehub.support;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class RequestsGeneratorPanelFactory implements IBreadCrumbPanelFactory {

    @Override
    public BreadCrumbPanel create(final String componentId, final IBreadCrumbModel breadCrumbModel) {
        return new RequestsGeneratorPanel(componentId, breadCrumbModel);
    }
}
