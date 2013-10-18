package com.axiell.ehub.provider.record.format;

import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.DeleteLink;
import com.axiell.ehub.language.Language;

final class TextsDeleteLink extends DeleteLink<Void> {
    private static final String CONFIRMATION_TEXTS = "Are you sure you want to delete these texts?";    
    private final Language language;
    private final IModel<FormatDecoration> formatModel;
    private final FormatDecorationMediator formatDecorationMediator;    
    
    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    TextsDeleteLink(final String id, final Language language, final IModel<FormatDecoration> formatModel, final FormatDecorationMediator formatDecorationMediator) {
	super(id, CONFIRMATION_TEXTS);	
	this.language = language;	
	this.formatModel = formatModel;
	this.formatDecorationMediator = formatDecorationMediator;
    }

    @Override
    public void onClick() {
        deleteTexts();
        formatDecorationMediator.afterDeleteTexts();
    }

    private void deleteTexts() {
	final FormatDecoration formatDecoration = formatModel.getObject();
        final Map<String, FormatTextBundle> textBundles = formatDecoration.getTextBundles();
        final String languageId = language.getId();
        final FormatTextBundle textBundle = textBundles.get(languageId);

        if (textBundle != null) {
            formatAdminController.delete(textBundle);
        }
    }
}