package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.language.Language;

class NameModel extends AbstractTextModel {    

    NameModel(final IModel<FormatDecoration> formModel, final Language language) {
        super(formModel, language);
    }

    @Override
    String getText(FormatTextBundle textBundle) {
        return textBundle.getName();
    }

    @Override
    void setText(FormatTextBundle textBundle, String text) {
        textBundle.setName(text);
    }
}