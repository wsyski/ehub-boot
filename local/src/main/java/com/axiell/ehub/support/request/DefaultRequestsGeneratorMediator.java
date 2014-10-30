package com.axiell.ehub.support.request;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

class DefaultRequestsGeneratorMediator extends AbstractRequestsGeneratorMediator<DefaultSupportResponse> {

    @Override
    protected Panel makeResponsePanel(String panelId, DefaultSupportResponse response) {
        return new ResponsePanel(panelId, response);
    }

    @Override
    protected void afterResponsePanelIsReplaced(DefaultSupportResponse response, AjaxRequestTarget target) {
        target.appendJavascript("PR.prettyPrint()");
    }
}
