package com.axiell.ehub.support.request;

import com.axiell.ehub.support.request.format.GetFormatsRequestsPanelFactory;
import com.axiell.ehub.support.request.loan.CreateLoanRequestsPanelFactory;
import com.axiell.ehub.support.request.loan.GetLoanRequestsPanelFactory;
import com.axiell.ehub.support.request.patron.PatronIdGeneratorPanelFactory;
import com.axiell.ehub.support.request.record.GetRecordRequestsPanelFactory;
import com.axiell.ehub.support.request.record.GetRecordRequestsPanelFactory_v2;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

class RequestsGeneratorPanel extends BreadCrumbPanel {

    RequestsGeneratorPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addGetRecordRequestsPanelLink(breadCrumbModel);
        addGetRecordRequestsPanelLink_v2(breadCrumbModel);
        addGetFormatsRequestsPanelLink(breadCrumbModel);
        addCreateLoanRequestsPanelLink(breadCrumbModel);
        addGetLoanRequestsPanelLink(breadCrumbModel);
        addPatronIdGeneratorPanelLink(breadCrumbModel);
    }

    private void addGetRecordRequestsPanelLink(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new GetRecordRequestsPanelFactory();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("getRecordLink", breadCrumbModel, factory);
        add(link);
    }

    private void addGetRecordRequestsPanelLink_v2(IBreadCrumbModel breadCrumbModel) {
        final IBreadCrumbPanelFactory factory = new GetRecordRequestsPanelFactory_v2();
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("getRecordLink_v2", breadCrumbModel, factory);
        add(link);
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
