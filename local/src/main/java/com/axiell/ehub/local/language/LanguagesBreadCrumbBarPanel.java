package com.axiell.ehub.local.language;

import com.axiell.ehub.local.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class LanguagesBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<LanguagesPanel> {

    public LanguagesBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public LanguagesPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
        return new LanguagesPanel(activePanelId, breadCrumbModel);
    }
}
