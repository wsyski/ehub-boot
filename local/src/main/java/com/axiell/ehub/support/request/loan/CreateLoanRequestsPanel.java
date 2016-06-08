package com.axiell.ehub.support.request.loan;

import com.axiell.ehub.support.request.AbstractRequestsGeneratorPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

class CreateLoanRequestsPanel extends AbstractRequestsGeneratorPanel {

    CreateLoanRequestsPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        form.addEhubConsumerChoice();
        form.addPatronId();
        form.addLibraryCard();
        form.addPin();
        form.addContentProviderNameField();
        form.addContentProviderRecordIdField();
        form.addLangaugeField();
        form.addLmsRecordIdField();
        form.addFormatIdField();
        addGeneratorButton();
    }

    private void addGeneratorButton() {
        final CreateLoanButton generatorButton = new CreateLoanButton("generatorButton", mediator);
        form.add(generatorButton);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
