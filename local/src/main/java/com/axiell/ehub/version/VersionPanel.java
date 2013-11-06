package com.axiell.ehub.version;

import javax.servlet.ServletContext;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class VersionPanel extends BreadCrumbPanel {

    @SpringBean(name = "releaseAdminController")
    private IReleaseAdminController releaseAdminController;

    VersionPanel(final String panelId, final IBreadCrumbModel breadCrumbModel) {
	super(panelId, breadCrumbModel);
	addDatabaseInfo();
	addManifestInfo();
    }

    private void addDatabaseInfo() {
	IModel<String> databaseVersionModel;
	IModel<String> databaseModifiedDateModel;

	try {
	    final Release release = releaseAdminController.getLatestDatabaseRelease();
	    databaseVersionModel = new Model<>(release.getVersion());
	    databaseModifiedDateModel = new Model<>(release.getModifiedDate().toString());
	} catch (EmptyReleaseTableException e) {
	    databaseVersionModel = notAvailableModel();
	    databaseModifiedDateModel = notAvailableModel();
	}

	addDatabaseVersion(databaseVersionModel);
	addDatabaseModifiedDate(databaseModifiedDateModel);
    }

    private void addDatabaseVersion(final IModel<String> databaseVersionModel) {
	addLabel("databaseVersion", databaseVersionModel);
    }

    private void addLabel(final String id, final IModel<String> labelModel) {
	final Label label = new Label(id, labelModel);
	add(label);
    }

    private void addDatabaseModifiedDate(final IModel<String> databaseModifiedDateModel) {
	addLabel("databaseModifiedDate", databaseModifiedDateModel);
    }

    private void addManifestInfo() {
	IModel<String> warFileVersionModel;
	IModel<String> warFileBuildTimeModel;

	try {
	    final ServletContext servletContext = WebApplication.get().getServletContext();
	    final WarFileManifest warFileManifest = WarFileManifest.read(servletContext);
	    warFileVersionModel = makeManifestAttributeModel(warFileManifest.getImplementationVersion());
	    warFileBuildTimeModel = makeManifestAttributeModel(warFileManifest.getBuildTime());
	} catch (ManifestException e) {
	    warFileVersionModel = notAvailableModel();
	    warFileBuildTimeModel = notAvailableModel();
	}

	addWarFileVersion(warFileVersionModel);
	addWarFileBuildTime(warFileBuildTimeModel);
    }

    private IModel<String> makeManifestAttributeModel(final String attributeValue) {
	return attributeValue == null ? notAvailableModel() : new Model<String>(attributeValue);
    }

    private void addWarFileVersion(final IModel<String> warFileVersionModel) {
	addLabel("warFileVersion", warFileVersionModel);
    }

    private void addWarFileBuildTime(final IModel<String> warFileBuildTimeModel) {
	addLabel("warFileBuildTime", warFileBuildTimeModel);
    }

    @Override
    public String getTitle() {
	final StringResourceModel model = new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
	return model.getString();
    }

    private StringResourceModel notAvailableModel() {
	return new StringResourceModel("msgNotAvailable", this, new Model<>());
    }
}
