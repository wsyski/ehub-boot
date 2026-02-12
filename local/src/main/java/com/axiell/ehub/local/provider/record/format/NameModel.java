package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;
import org.apache.wicket.model.IModel;

class NameModel extends AbstractTextModel {

    NameModel(final IModel<FormatDecoration> formModel, final Language language) {
        super(formModel, language);
    }

    @Override
    String getText(final FormatTextBundle textBundle) {
        return textBundle.getName();
    }

    @Override
    void setText(final FormatTextBundle textBundle, final String text) {
        textBundle.setName(text);
    }
}
