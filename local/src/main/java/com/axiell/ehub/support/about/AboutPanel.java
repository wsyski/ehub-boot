package com.axiell.ehub.support.about;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class AboutPanel extends BreadCrumbPanel {

    public AboutPanel(final String id, final IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addManifestInfo();
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
    }

    private void addManifestInfo() {
        final WarFileManifest warFileManifest = new WarFileManifest();
        addLabel(warFileManifest, "implementationBuild", "warFileBuild");
        addLabel(warFileManifest, "implementationVersion", "warFileVersion");
        addLabel(warFileManifest, "buildTime", "warFileBuildTime");
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
