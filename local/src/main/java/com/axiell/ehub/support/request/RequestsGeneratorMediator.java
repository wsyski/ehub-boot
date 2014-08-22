package com.axiell.ehub.support.request;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import java.io.Serializable;

public class RequestsGeneratorMediator implements Serializable {
    private Panel responsePanel;

    public void registerResponsePanel(Panel authInfoPanel) {
        this.responsePanel = authInfoPanel;
    }

    public void afterResponseWasReceived(final SupportResponse response, final AjaxRequestTarget target) {
        replaceResponsePanel(response, target);
        target.appendJavascript("PR.prettyPrint()");
    }

    private void replaceResponsePanel(final SupportResponse response, final AjaxRequestTarget target) {
        final ResponsePanel newResponsePanel = new ResponsePanel(responsePanel.getId(), response);
        responsePanel.replaceWith(newResponsePanel);
        responsePanel = newResponsePanel;
        target.addComponent(responsePanel);
    }
}
