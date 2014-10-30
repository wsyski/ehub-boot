package com.axiell.ehub.support.request;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

public interface IRequestsGeneratorMediator<R extends ISupportResponse> {

    void registerResponsePanel(Panel responsePanel);

    void afterResponseWasReceived(R response, AjaxRequestTarget target);
}
