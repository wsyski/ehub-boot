package com.axiell.ehub.provider.record.format;

import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.provider.ContentProviderMediator;

final class TextsSaveButton extends Button {
    private final IModel<FormatDecoration> formModel;
    private final ContentProviderMediator contentProviderMediator;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    TextsSaveButton(final String id, final IModel<FormatDecoration> formModel, final ContentProviderMediator contentProviderMediator) {
	super(id);
	this.formModel = formModel;
	this.contentProviderMediator = contentProviderMediator;	
    }

    @Override
    public void onSubmit() {
	saveTexts();
	contentProviderMediator.afterSavedTextxs();
    }

    private void saveTexts() {
	final FormatDecoration formatDecoration = formModel.getObject();
	Map<String, FormatTextBundle> textBundles = formatDecoration.getTextBundles();

	for (FormatTextBundle textBundle : textBundles.values()) {
	    formatAdminController.save(textBundle);
	}

	formatAdminController.save(formatDecoration);
    }
}