package com.axiell.ehub.support.request;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

public class GetFormatsRequestsPanelFactory implements IBreadCrumbPanelFactory {

    @Override
    public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
        return new GetFormatsRequestsPanel(componentId, breadCrumbModel);
    }
}
