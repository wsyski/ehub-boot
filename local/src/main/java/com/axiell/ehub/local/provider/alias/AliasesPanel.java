package com.axiell.ehub.local.provider.alias;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

class AliasesPanel extends BreadCrumbPanel {
    private final AliasMediator mediator;

    AliasesPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        setOutputMarkupId(true);
        mediator = new AliasMediator();
        mediator.registerAliasesPanel(this);
        addAliasModalWindowPanel();
        addOrReplaceAliasListView();
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }

    @Override
    public void onActivate(final IBreadCrumbParticipant previous) {
        addOrReplaceAliasListView();
        super.onActivate(previous);
    }

    private void addAliasModalWindowPanel() {
        final AliasModalWindowPanel aliasModalWindowPanel = new AliasModalWindowPanel("aliasModalWindow", mediator);
        add(aliasModalWindowPanel);
    }

    private void addOrReplaceAliasListView() {
        final AliasListView aliasListView = new AliasListView("aliases", mediator);
        addOrReplace(aliasListView);
    }
}
