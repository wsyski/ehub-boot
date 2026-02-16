package com.axiell.ehub.local.provider.alias;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.io.Serializable;
import java.util.Optional;

public class AliasMediator implements Serializable {
    private AliasesPanel aliasesPanel;
    private ModalDialog createModalDialog;
    private FeedbackPanel createFeedbackPanel;

    public void registerAliasesPanel(final AliasesPanel aliasesPanel) {
        this.aliasesPanel = aliasesPanel;
    }

    public void registerCreateModalDialog(final ModalDialog modalDialog) {
        this.createModalDialog = modalDialog;
    }

    public void registerCreateFeedbackPanel(final FeedbackPanel feedback) {
        this.createFeedbackPanel = feedback;
    }

    public void showCreateWindow(final Optional<AjaxRequestTarget> targetOptional) {
        targetOptional.ifPresent(target -> createModalDialog.open(target));
    }

    public void afterAliasWasCreated(final AjaxRequestTarget target) {
        createModalDialog.close(target);
        replaceCurrentAliasesPanelWithNewInstance(target);
    }

    public void onCreateError(final AjaxRequestTarget target) {
        if (target != null)
            target.add(createFeedbackPanel);
    }

    public void afterAliasWasDeleted() {
        aliasesPanel.activate(aliasesPanel);
    }

    private void replaceCurrentAliasesPanelWithNewInstance(final AjaxRequestTarget target) {
        final AliasesPanel newAliasesPanel = new AliasesPanel(aliasesPanel.getId(), aliasesPanel.getBreadCrumbModel());
        aliasesPanel.replaceWith(newAliasesPanel);
        aliasesPanel = newAliasesPanel;

        if (target != null)
            target.add(newAliasesPanel);
    }
}
