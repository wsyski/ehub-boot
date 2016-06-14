package com.axiell.ehub.error;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import com.axiell.ehub.ErrorCauseArgumentType;

class ErrorCauseArgumentValuesListView extends ListView<ErrorCauseArgumentType> {
    private final IBreadCrumbModel breadCrumbModel;

    @SpringBean(name = "errorCauseArgumentValueAdminController")
    private IErrorCauseArgumentValueAdminController errorCauseArgumentValueAdminController;

    ErrorCauseArgumentValuesListView(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id);
        this.breadCrumbModel = breadCrumbModel;
        setErrorCauseArgumentValueTypes();
    }

    private void setErrorCauseArgumentValueTypes() {
        final List<ErrorCauseArgumentType> types = errorCauseArgumentValueAdminController.getTypes();
        setList(types);
    }

    @Override
    protected void populateItem(final ListItem<ErrorCauseArgumentType> item) {
        final ErrorCauseArgumentType type = item.getModelObject();
        final BreadCrumbPanelLink link = makeErrorCauseArgumentValueLink(type);
        item.add(link);
    }

    private BreadCrumbPanelLink makeErrorCauseArgumentValueLink(final ErrorCauseArgumentType type) {
        final IBreadCrumbPanelFactory factory = new ErrorCauseArgumentValuePanelFactory(type);
        final BreadCrumbPanelLink link = new BreadCrumbPanelLink("errorCauseArgumentValueLink", breadCrumbModel, factory);
        final Label linkLabel = makeErrorCauseArgumentLinkLabel(type);
        link.add(linkLabel);
        return link;
    }

    private Label makeErrorCauseArgumentLinkLabel(final ErrorCauseArgumentType type) {
        final String name = type.name();
        return new Label("errorCauseArgumentValueLinkLabel", name);
    }
}
