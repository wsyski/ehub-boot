package com.axiell.ehub.provider.record.format;

import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class TextsSaveButton extends Button {
    private final IModel<FormatDecoration> formModel;
    private final FormatDecorationMediator formatDecorationMediator;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    TextsSaveButton(final String id, final IModel<FormatDecoration> formModel, final FormatDecorationMediator formatDecorationMediator) {
	super(id);
	this.formModel = formModel;
	this.formatDecorationMediator = formatDecorationMediator;	
    }

    @Override
    public void onSubmit() {
	saveTexts();
	formatDecorationMediator.afterSavedTexts();
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