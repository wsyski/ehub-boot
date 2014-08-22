package com.axiell.ehub.support.request;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.panel.EmptyPanel;

public abstract class AbstractRequestsGeneratorPanel extends BreadCrumbPanel {
    protected RequestsGeneratorForm form;
    protected RequestsGeneratorMediator mediator;

    protected AbstractRequestsGeneratorPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        this.mediator = new RequestsGeneratorMediator();
        addRequestForm();
        addResponsePanel();
    }

    private void addRequestForm() {
        form = new RequestsGeneratorForm("requestsGeneratorForm");
        add(form);
    }

    private void addResponsePanel() {
        final EmptyPanel responsePanel = new EmptyPanel("responsePanel");
        responsePanel.setOutputMarkupId(true);
        mediator.registerResponsePanel(responsePanel);
        add(responsePanel);
    }
}
