package com.axiell.ehub.support.request.loan;

import com.axiell.ehub.support.request.AbstractRequestsGeneratorPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

class GetLoanRequestsPanel extends AbstractRequestsGeneratorPanel {

    GetLoanRequestsPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        form.addLangaugeField();
        form.addLmsLoanIdField();
        addGeneratorButton();
    }

    private void addGeneratorButton() {
        final GetLoanButton generatorButton = new GetLoanButton("generatorButton", mediator);
        form.add(generatorButton);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
