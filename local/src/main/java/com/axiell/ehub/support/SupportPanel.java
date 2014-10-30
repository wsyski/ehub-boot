package com.axiell.ehub.support;

import com.axiell.ehub.support.about.AboutPanelFactory;
import com.axiell.ehub.support.log.LogFilesPanelFactory;
import com.axiell.ehub.support.request.patron.PatronIdGeneratorPanelFactory;
import com.axiell.ehub.support.request.RequestsGeneratorPanelFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class SupportPanel extends BreadCrumbPanel {

    SupportPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addAboutPanelLink(breadCrumbModel);
        addLogFilesPanelLink(breadCrumbModel);
        addRequestsGeneratorPanelLink(breadCrumbModel);
    }

    private void addAboutPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new AboutPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("aboutLink", breadCrumbModel, factory);
        add(link);
    }

    private void addLogFilesPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new LogFilesPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("logFilesLink", breadCrumbModel, factory);
        add(link);
    }

    private void addRequestsGeneratorPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new RequestsGeneratorPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("requestsGeneratorLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
