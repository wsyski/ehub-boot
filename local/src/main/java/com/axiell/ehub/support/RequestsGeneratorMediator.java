package com.axiell.ehub.support;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import java.io.Serializable;

class RequestsGeneratorMediator implements Serializable {
    private Panel responsePanel;

    void registerResponsePanel(Panel authInfoPanel) {
        this.responsePanel = authInfoPanel;
    }

    void afterResponseWasGenerated(final SupportResponse response, final AjaxRequestTarget target) {
        replaceResponsePanel(response, target);
        submitResponseBodyForm(target);
    }

    private void replaceResponsePanel(final SupportResponse response, final AjaxRequestTarget target) {
        final ResponsePanel newResponsePanel = new ResponsePanel(responsePanel.getId(), response);
        responsePanel.replaceWith(newResponsePanel);
        responsePanel = newResponsePanel;
        target.addComponent(responsePanel);
    }

    private void submitResponseBodyForm(final AjaxRequestTarget target) {
        target.appendJavascript("window.document.getElementById('reponse-body-form').submit();");
    }
}
