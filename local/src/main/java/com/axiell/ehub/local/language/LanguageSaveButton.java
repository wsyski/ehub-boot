package com.axiell.ehub.local.language;

import com.axiell.ehub.common.language.Language;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class LanguageSaveButton extends Button {
    private final IModel<Language> formModel;
    private LanguagesMediator mediator;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    LanguageSaveButton(final String id, final IModel<Language> formModel, final LanguagesMediator mediator) {
        super(id);
        this.formModel = formModel;
        this.mediator = mediator;
    }

    @Override
    public void onSubmit() {
        final Language language = formModel.getObject();
        languageAdminController.save(language);
        mediator.afterSaveLanguage();
    }
}
