package com.axiell.ehub.provider.record.format;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

class FormatDecorationMediator implements Serializable, IContentDispositionChangedAwareMediator {
    private FormatDecorationPanel formatDecorationPanel;
    private PlayerContainer playerContainer;
    
    void registerFormatDecorationPanel(final FormatDecorationPanel formatDecorationPanel) {
	this.formatDecorationPanel = formatDecorationPanel;
    }
    
    @Override
    public void registerPlayerContainer(final PlayerContainer playerContainer) {
	this.playerContainer = playerContainer;
    }
    
    void afterSavedTexts() {
	formatDecorationPanel.activate(formatDecorationPanel);
    }

    void afterDeleteTexts() {
	formatDecorationPanel.activate(formatDecorationPanel);
    }
    
    @Override
    public void afterContentDispositionChanged(final AjaxRequestTarget target) {
	if (target != null) {
	    target.addComponent(playerContainer);
	}	   
    }
    
    void afterEditFormatDecoration() {
	formatDecorationPanel.activate(formatDecorationPanel);
    }
}
