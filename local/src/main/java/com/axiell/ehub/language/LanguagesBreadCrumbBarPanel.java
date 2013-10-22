package com.axiell.ehub.language;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

import com.axiell.ehub.AbstractBreadCrumbBarPanel;

public class LanguagesBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<LanguagesPanel> {

    public LanguagesBreadCrumbBarPanel(final String panelId) {
	super(panelId);
    }

    @Override
    public LanguagesPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	return new LanguagesPanel(activePanelId, breadCrumbModel);
    }
}
