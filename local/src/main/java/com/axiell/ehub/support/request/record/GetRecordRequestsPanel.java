package com.axiell.ehub.support.request.record;

import com.axiell.ehub.support.request.AbstractRequestsGeneratorPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

class GetRecordRequestsPanel extends AbstractRequestsGeneratorPanel {

    GetRecordRequestsPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        form.addEhubConsumerChoice();
        form.addPatronId();
        form.addLibraryCard();
        form.addPin();
        form.addContentProviderNameField();
        form.addContentProviderRecordIdField();
        form.addLangaugeField();
        addGeneratorButton();
    }

    private void addGeneratorButton() {
        final GetRecordButton generatorButton = new GetRecordButton("generatorButton", mediator);
        form.add(generatorButton);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
