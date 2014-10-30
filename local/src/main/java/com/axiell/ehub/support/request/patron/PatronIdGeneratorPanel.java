package com.axiell.ehub.support.request.patron;

import com.axiell.ehub.support.request.AbstractRequestsGeneratorPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

class PatronIdGeneratorPanel extends AbstractRequestsGeneratorPanel {

    PatronIdGeneratorPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel, new PatronIdGeneratorMediator());
        form.addLibraryCard();
        addGeneratorButton();
    }

    private void addGeneratorButton() {
        final PatronIdGeneratorButton generatorButton = new PatronIdGeneratorButton("generatorButton", mediator);
        form.add(generatorButton);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
