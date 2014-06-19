package com.axiell.ehub.support;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.CompoundPropertyModel;

class RequestsGeneratorPanel extends BreadCrumbPanel {

    RequestsGeneratorPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addGetFormatsRequestsPanelLink(breadCrumbModel);



//        final RequestsGeneratorMediator mediator = new RequestsGeneratorMediator();
//
//        final EmptyPanel responsePanel = new EmptyPanel("responsePanel");
//        responsePanel.setOutputMarkupId(true);
//        mediator.registerResponsePanel(responsePanel);
//        add(responsePanel);
//
//        final Form<?> form = new Form<>("requestsGeneratorForm", new CompoundPropertyModel<>(new RequestParameters()));
//        add(form);
//
//
//
//
//        final AbstractRequestGeneratorButton generatorButton = new AbstractRequestGeneratorButton("generatorButton", mediator);
//        form.add(generatorButton);
    }

    private void addGetFormatsRequestsPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new GetFormatsRequestsPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("getFormatsLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
