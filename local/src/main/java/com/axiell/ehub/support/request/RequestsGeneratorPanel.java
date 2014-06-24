package com.axiell.ehub.support.request;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class RequestsGeneratorPanel extends BreadCrumbPanel {

    RequestsGeneratorPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addGetFormatsRequestsPanelLink(breadCrumbModel);
    }

    private void addGetFormatsRequestsPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new GetFormatsRequestsPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("getFormatsLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
