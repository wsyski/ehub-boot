package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.language.Language;

class DescriptionModel extends AbstractTextModel {
    
    DescriptionModel(final IModel<FormatDecoration> formModel, final Language language) {
	super(formModel, language);
    }

    @Override
    String getText(FormatTextBundle textBundle) {
        return textBundle.getDescription();
    }

    @Override
    void setText(FormatTextBundle textBundle, String text) {
        textBundle.setDescription(text);
    }
}