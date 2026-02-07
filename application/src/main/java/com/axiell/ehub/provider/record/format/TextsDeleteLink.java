package com.axiell.ehub.provider.record.format;

import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.ConfirmationLink;
import com.axiell.ehub.language.Language;

final class TextsDeleteLink extends ConfirmationLink<Void> {   
    private final Language language;
    private final IModel<FormatDecoration> formModel;
    private final FormatDecorationMediator formatDecorationMediator;    
    
    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    TextsDeleteLink(final String id, final Language language, final IModel<FormatDecoration> formModel, final FormatDecorationMediator formatDecorationMediator) {
	super(id);	
	this.language = language;	
	this.formModel = formModel;
	this.formatDecorationMediator = formatDecorationMediator;
    }

    @Override
    public void onClick() {
        deleteTexts();
        formatDecorationMediator.afterDeleteTexts();
    }

    private void deleteTexts() {
	final FormatDecoration formatDecoration = formModel.getObject();
        final Map<Language, FormatTextBundle> textBundles = formatDecoration.getTextBundles();
        final FormatTextBundle textBundle = textBundles.get(language);

        if (textBundle != null) {
            formatAdminController.delete(textBundle);
        }
    }
}