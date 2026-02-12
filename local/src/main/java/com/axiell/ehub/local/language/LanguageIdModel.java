package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import org.apache.wicket.model.IModel;

class LanguageIdModel implements IModel<String> {
    private final IModel<Language> formModel;

    LanguageIdModel(final IModel<Language> formModel) {
        this.formModel = formModel;
    }

    @Override
    public String getObject() {
        Language language = formModel.getObject();
        return language.getId();
    }

    @Override
    public void setObject(String id) {
        Language language = formModel.getObject();
        language.setId(id);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
