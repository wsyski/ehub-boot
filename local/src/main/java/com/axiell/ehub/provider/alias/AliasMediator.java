package com.axiell.ehub.provider.alias;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.io.Serializable;

class AliasMediator implements Serializable {
    private AliasesPanel aliasesPanel;
    private AliasCreateModalWindow createModalWindow;
    private FeedbackPanel createFeedbackPanel;

    public void registerAliasesPanel(final AliasesPanel aliasesPanel) {
        this.aliasesPanel = aliasesPanel;
    }

    void registerCreateModalWindow(final AliasCreateModalWindow modalWindow) {
        this.createModalWindow = modalWindow;
    }

    void registerCreateFeedbackPanel(final FeedbackPanel feedback) {
        this.createFeedbackPanel = feedback;
    }

    void showCreateWindow(final AjaxRequestTarget target) {
        createModalWindow.show(target);
    }

    void afterAliasWasCreated(final AjaxRequestTarget target) {
        createModalWindow.close(target);
        replaceCurrentAliasesPanelWithNewInstance(target);
    }

    void onCreateError(final AjaxRequestTarget target) {
        if (target != null)
            target.addComponent(createFeedbackPanel);
    }

    void afterAliasWasDeleted() {
        aliasesPanel.activate(aliasesPanel);
    }

    private void replaceCurrentAliasesPanelWithNewInstance(final AjaxRequestTarget target) {
        final AliasesPanel newAliasesPanel = new AliasesPanel(aliasesPanel.getId(), aliasesPanel.getBreadCrumbModel());
        aliasesPanel.replaceWith(newAliasesPanel);
        aliasesPanel = newAliasesPanel;

        if (target != null)
            target.addComponent(newAliasesPanel);
    }
}
