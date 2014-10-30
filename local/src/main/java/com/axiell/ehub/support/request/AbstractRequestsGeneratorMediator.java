package com.axiell.ehub.support.request;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import java.io.Serializable;

public abstract class AbstractRequestsGeneratorMediator<R extends ISupportResponse> implements IRequestsGeneratorMediator<R>, Serializable {
    private Panel responsePanel;

    @Override
    public void registerResponsePanel(Panel responsePanel) {
        this.responsePanel = responsePanel;
    }

    @Override
    public void afterResponseWasReceived(R response, AjaxRequestTarget target) {
        replaceResponsePanel(response, target);
        afterResponsePanelIsReplaced(response, target);
    }

    private void replaceResponsePanel(final R response, final AjaxRequestTarget target) {
        final Panel newResponsePanel = makeResponsePanel(responsePanel.getId(), response);
        responsePanel.replaceWith(newResponsePanel);
        responsePanel = newResponsePanel;
        target.addComponent(responsePanel);
    }

    protected abstract Panel makeResponsePanel(String panelId, R response);

    protected void afterResponsePanelIsReplaced(R response, AjaxRequestTarget target) {
    }
}
