package com.axiell.ehub.local.consumer;

import com.axiell.ehub.local.AbstractBreadCrumbBarPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class EhubConsumersBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<EhubConsumersPanel> {

    public EhubConsumersBreadCrumbBarPanel(final String panelId) {
        super(panelId);
    }

    @Override
    public EhubConsumersPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
        return new EhubConsumersPanel(activePanelId, breadCrumbModel);
    }
}
