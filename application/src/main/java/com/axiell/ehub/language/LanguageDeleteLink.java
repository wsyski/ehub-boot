package com.axiell.ehub.language;

import com.axiell.ehub.ConfirmationLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class LanguageDeleteLink extends ConfirmationLink<Void> {
    private final Language language;
    private final LanguagesMediator mediator;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    LanguageDeleteLink(final String id, final Language language, final LanguagesMediator mediator) {
        super(id);
        this.language = language;
        this.mediator = mediator;
    }

    @Override
    public void onClick() {
        try {
            languageAdminController.delete(language);
        } catch (LanguageReferencedException ex) {
            final IModel<LanguageReferencedException> substitutionModel = new Model<>(ex);
            final StringResourceModel resourceModel = new StringResourceModel("msgCouldNotDeleteLanguage", this, substitutionModel);
            final String message = resourceModel.getString();
            error(message);
        }
        mediator.afterDeleteLanguage();
    }
}