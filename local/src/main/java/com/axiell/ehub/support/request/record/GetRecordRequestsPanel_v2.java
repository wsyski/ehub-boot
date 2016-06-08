package com.axiell.ehub.support.request.record;

import com.axiell.ehub.support.request.AbstractRequestsGeneratorPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

class GetRecordRequestsPanel_v2 extends AbstractRequestsGeneratorPanel {

    GetRecordRequestsPanel_v2(String id, IBreadCrumbModel breadCrumbModel) {
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
        final GetRecordButton_v2 generatorButton = new GetRecordButton_v2("generatorButton", mediator);
        form.add(generatorButton);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
