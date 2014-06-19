package com.axiell.ehub.support;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

class GetFormatsRequestsPanel extends AbstractRequestsGeneratorPanel {

    GetFormatsRequestsPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        form.addContentProviderNameField();
        form.addContentProviderRecordIdField();
        form.addLangaugeField();
        addGeneratorButton();
    }

    private void addGeneratorButton() {
        final GetFormatsButton generatorButton = new GetFormatsButton("generatorButton", mediator);
        form.add(generatorButton);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
