package com.axiell.ehub.support.about;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

class AboutPanel extends BreadCrumbPanel {

    @SpringBean(name = "databaseChangeLogAdminController")
    private IDatabaseChangeLogAdminController databaseChangeLogAdminController;

    AboutPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addManifestInfo();
        addDatabaseInfo();
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }

    private void addManifestInfo() {
        final WarFileManifest warFileManifest = WarFileManifestRetriever.retrieve();
        addLabel(warFileManifest, "implementationVersion", "warFileVersion");
        addLabel(warFileManifest, "buildTime", "warFileBuildTime");
    }

    private void addDatabaseInfo() {
        final DatabaseChangeLog latestDatabaseChange = databaseChangeLogAdminController.getLatestDatabaseChange();
        addLabel(latestDatabaseChange, "id", "id");
        addLabel(latestDatabaseChange, "author", "author");
        addLabel(latestDatabaseChange, "fileName", "fileName");
        addLabel(latestDatabaseChange, "dateExecuted", "dateExecuted");
        addLabel(latestDatabaseChange, "orderExecuted", "orderExecuted");
        addLabel(latestDatabaseChange, "comments", "comments");
        addLabel(latestDatabaseChange, "tag", "tag");
    }

    private void addLabel(final Object modelObject, final String expression, final String labelId) {
        final IModel<String> model = makeModel(modelObject, expression);
        addLabel(labelId, model);
    }

    private IModel<String> makeModel(final Object modelObject, final String expression) {
        return modelObject == null ? notAvailableModel() : new NotAvailableFallbackPropertyModel(modelObject, expression);
    }

    private StringResourceModel notAvailableModel() {
        return new StringResourceModel("msgNotAvailable", this, new Model<>());
    }

    private void addLabel(final String id, final IModel<String> labelModel) {
        final Label label = new Label(id, labelModel);
        add(label);
    }

    private class NotAvailableFallbackPropertyModel extends PropertyModel<String> {

        private NotAvailableFallbackPropertyModel(Object modelObject, String expression) {
            super(modelObject, expression);
        }

        @Override
        public String getObject() {
            final Object object = super.getObject();
            return object == null ? getString("msgNotAvailable") : object.toString();
        }
    }
}
