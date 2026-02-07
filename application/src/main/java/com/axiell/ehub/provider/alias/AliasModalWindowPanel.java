package com.axiell.ehub.provider.alias;

import org.apache.wicket.markup.html.panel.Panel;

class AliasModalWindowPanel extends Panel {

    AliasModalWindowPanel(final String id, final AliasMediator mediator) {
        super(id);
        addAliasCreateWindowShowLink(mediator);
        addAliasCreateModalWindow(mediator);
    }

    private void addAliasCreateWindowShowLink(AliasMediator mediator) {
        final AliasCreateWindowShowLink showLink = new AliasCreateWindowShowLink("showLink", mediator);
        add(showLink);
    }

    private void addAliasCreateModalWindow(AliasMediator mediator) {
        final AliasCreateModalWindow modalWindow = new AliasCreateModalWindow("window", mediator);
        mediator.registerCreateModalWindow(modalWindow);
        add(modalWindow);
    }
}
