package com.axiell.ehub.support;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class SupportPanel extends BreadCrumbPanel {

    SupportPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addAuthenticationHeaderPanelLink(breadCrumbModel);
    }

    private void addAuthenticationHeaderPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new RequestsGeneratorPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("requestsGeneratorLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
