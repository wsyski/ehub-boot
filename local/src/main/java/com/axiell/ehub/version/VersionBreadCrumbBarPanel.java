package com.axiell.ehub.version;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

import com.axiell.ehub.AbstractBreadCrumbBarPanel;

public class VersionBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<VersionPanel> {
    
    public VersionBreadCrumbBarPanel(final String panelId) {
	super(panelId);
    }

    @Override
    public VersionPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	return new VersionPanel(activePanelId, breadCrumbModel);
    }
}
