package com.axiell.ehub.local.provider.alias;

import org.apache.wicket.markup.html.panel.Panel;

class AliasCreatePanel extends Panel {

    AliasCreatePanel(final String id, final AliasMediator mediator) {
        super(id);
        final AliasCreateForm createForm = new AliasCreateForm("createForm", mediator);
        add(createForm);
    }
}
