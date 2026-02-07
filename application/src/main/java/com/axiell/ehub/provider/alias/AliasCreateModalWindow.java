package com.axiell.ehub.provider.alias;


class AliasCreateModalWindow extends ModalWindow {

    AliasCreateModalWindow(final String id, final AliasMediator mediator) {
        super(id);
        setCssClassName(ModalWindow.CSS_CLASS_GRAY);
        setTitle();
        setContent(mediator);
    }

    private void setTitle() {
        final String title = getString("txtTitle");
        setTitle(title);
    }

    private void setContent(final AliasMediator mediator) {
        final AliasCreatePanel createPanel = new AliasCreatePanel(getContentId(), mediator);
        setContent(createPanel);
    }
}
