package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.ConfirmationLink;
import com.axiell.ehub.provider.ContentProviderMediator;

public final class FormatDecorationDeleteLink extends ConfirmationLink<Void> {
    private final FormatDecoration formatDecoration;
    private final ContentProviderMediator contentProviderMediator;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    public FormatDecorationDeleteLink(final String id, final FormatDecoration formatDecoration, final ContentProviderMediator contentProviderMediator) {
	super(id);
	this.formatDecoration = formatDecoration;
	this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onClick() {
	if (isFormatDecorationDeletable()) {
	    formatAdminController.delete(formatDecoration);
	    contentProviderMediator.afterDeleteFormatDecoration();
	} else {
	    addFormatCouldNotBeDeletedMessage();
	}
    }

    private boolean isFormatDecorationDeletable() {
	return formatAdminController.deletable(formatDecoration);
    }

    private void addFormatCouldNotBeDeletedMessage() {
	final IModel<FormatDecoration> substitutionModel = new Model<>(formatDecoration);
	final StringResourceModel resourceModel = new StringResourceModel("msgCouldNotDeleteFormat", this, substitutionModel);
	final String message = resourceModel.getString();
	error(message);
    }
}