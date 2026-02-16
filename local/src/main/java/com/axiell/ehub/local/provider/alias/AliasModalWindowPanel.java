package com.axiell.ehub.local.provider.alias;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.extensions.ajax.markup.html.modal.theme.DefaultTheme;
import org.apache.wicket.markup.html.panel.Panel;

public class AliasModalWindowPanel extends Panel {

    public AliasModalWindowPanel(final String id, final AliasMediator mediator) {
        super(id);
        addAliasCreateWindowShowLink(mediator);
        addAliasCreateModalDialog(mediator);
    }

    private void addAliasCreateWindowShowLink(final AliasMediator mediator) {
        final AliasCreateWindowShowLink showLink = new AliasCreateWindowShowLink("showLink", mediator);
        add(showLink);
    }

    private void addAliasCreateModalDialog(final AliasMediator mediator) {
        final ModalDialog modalDialog = new ModalDialog("window");
        modalDialog.add(new DefaultTheme());
        modalDialog.closeOnEscape();
        modalDialog.closeOnClick();
        modalDialog.setContent(new AliasCreatePanel(ModalDialog.CONTENT_ID, mediator));
        mediator.registerCreateModalDialog(modalDialog);
        add(modalDialog);
    }
}
