package com.axiell.ehub.provider.routing;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import static org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CSS_CLASS_GRAY;

class RoutingRuleCreateModalWindow extends ModalWindow {

    RoutingRuleCreateModalWindow(final String id, final RoutingRulesMediator mediator) {
        super(id);
        setCssClassName(ModalWindow.CSS_CLASS_GRAY);
        setTitle();
        setContent(mediator);
    }

    private void setTitle() {
        final String title = getString("txtTitle");
        setTitle(title);
    }

    private void setContent(final RoutingRulesMediator mediator) {
        final RoutingRuleCreatePanel createPanel = new RoutingRuleCreatePanel(getContentId(), mediator);
        setContent(createPanel);
    }
}
