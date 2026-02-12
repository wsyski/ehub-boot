package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;
import org.apache.wicket.model.IModel;

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
