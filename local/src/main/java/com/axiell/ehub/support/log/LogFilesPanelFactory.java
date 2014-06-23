package com.axiell.ehub.support.log;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

public class LogFilesPanelFactory implements IBreadCrumbPanelFactory {

    @Override
    public BreadCrumbPanel create(final String componentId, final IBreadCrumbModel breadCrumbModel) {
        return new LogFilesPanel(componentId, breadCrumbModel);
    }
}
