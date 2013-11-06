package com.axiell.ehub.consumer;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

import com.axiell.ehub.AbstractBreadCrumbBarPanel;

public class EhubConsumersBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<EhubConsumersPanel> {

    public EhubConsumersBreadCrumbBarPanel(final String panelId) {
	super(panelId);
    }

    @Override
    public EhubConsumersPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	return new EhubConsumersPanel(activePanelId, breadCrumbModel);
    }
}
