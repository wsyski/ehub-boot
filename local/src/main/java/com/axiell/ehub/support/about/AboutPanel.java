package com.axiell.ehub.support.about;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.ServletContext;

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
        IModel<String> warFileVersionModel;
        IModel<String> warFileBuildTimeModel;

        try {
            final ServletContext servletContext = WebApplication.get().getServletContext();
            final WarFileManifest warFileManifest = WarFileManifest.read(servletContext);
            warFileVersionModel = makeModel(warFileManifest, "implementationVersion");
            warFileBuildTimeModel = makeModel(warFileManifest, "buildTime");
        } catch (ManifestException e) {
            warFileVersionModel = notAvailableModel();
            warFileBuildTimeModel = notAvailableModel();
        }

        addWarFileVersion(warFileVersionModel);
        addWarFileBuildTime(warFileBuildTimeModel);
    }

    private void addDatabaseInfo() {
        final DatabaseChangeLog latestDatabaseChange = databaseChangeLogAdminController.getLatestDatabaseChange();
        addDatabaseChangeLogInfoLabel(latestDatabaseChange, "id", "id");
        addDatabaseChangeLogInfoLabel(latestDatabaseChange, "fileName", "fileName");
        addDatabaseChangeLogInfoLabel(latestDatabaseChange, "dateExecuted", "dateExecuted");
        addDatabaseChangeLogInfoLabel(latestDatabaseChange, "orderExecuted", "orderExecuted");
        addDatabaseChangeLogInfoLabel(latestDatabaseChange, "comments", "comments");
        addDatabaseChangeLogInfoLabel(latestDatabaseChange, "tag", "tag");
    }

    private void addWarFileVersion(final IModel<String> warFileVersionModel) {
        addLabel("warFileVersion", warFileVersionModel);
    }

    private void addWarFileBuildTime(final IModel<String> warFileBuildTimeModel) {
        addLabel("warFileBuildTime", warFileBuildTimeModel);
    }

    private void addDatabaseChangeLogInfoLabel(final DatabaseChangeLog latestDatabaseChange, final String expression, final String labelId) {
        final IModel<String> model = makeModel(latestDatabaseChange, expression);
        addLabel(labelId, model);
    }

    private IModel<String> makeModel(final Object modelObject, final String expression) {
        return modelObject == null ? notAvailableModel() : new NotAvailableFallbackPropertyModel(modelObject, expression);
    }

    private void addLabel(final String id, final IModel<String> labelModel) {
        final Label label = new Label(id, labelModel);
        add(label);
    }

    private StringResourceModel notAvailableModel() {
        return new StringResourceModel("msgNotAvailable", this, new Model<>());
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
