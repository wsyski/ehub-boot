package com.axiell.ehub.provider;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

import com.axiell.ehub.AbstractBreadCrumbBarPanel;

public class ContentProvidersBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<ContentProvidersPanel> {

    public ContentProvidersBreadCrumbBarPanel(final String panelId) {
	super(panelId);
    }

    @Override
    public ContentProvidersPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	return new ContentProvidersPanel(activePanelId, breadCrumbModel);
    }
}
