package com.axiell.ehub.support.request;

import com.axiell.ehub.support.request.format.GetFormatsRequestsPanelFactory;
import com.axiell.ehub.support.request.loan.CreateLoanRequestsPanelFactory;
import com.axiell.ehub.support.request.loan.GetLoanRequestsPanelFactory;
import com.axiell.ehub.support.request.patron.PatronIdGeneratorPanelFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class RequestsGeneratorPanel extends BreadCrumbPanel {

    RequestsGeneratorPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addGetFormatsRequestsPanelLink(breadCrumbModel);
        addCreateLoanRequestsPanelLink(breadCrumbModel);
        addGetLoanRequestsPanelLink(breadCrumbModel);
        addPatronIdGeneratorPanelLink(breadCrumbModel);
    }

    private void addGetFormatsRequestsPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new GetFormatsRequestsPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("getFormatsLink", breadCrumbModel, factory);
        add(link);
    }

    private void addCreateLoanRequestsPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new CreateLoanRequestsPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("createLoanLink", breadCrumbModel, factory);
        add(link);
    }

    private void addGetLoanRequestsPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new GetLoanRequestsPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("getLoanLink", breadCrumbModel, factory);
        add(link);
    }

    private void addPatronIdGeneratorPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new PatronIdGeneratorPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("patronIdGeneratorLink", breadCrumbModel, factory);
        add(link);
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }
}
