package com.axiell.ehub.provider;

import com.axiell.ehub.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class ContentProvidersBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<ContentProvidersPanel> {

    public ContentProvidersBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public ContentProvidersPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
        return new ContentProvidersPanel(activePanelId, breadCrumbModel);
    }
}
