package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.AliasMapping;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

class AliasCreateForm extends StatelessForm<AliasMapping> implements Serializable {
    private final AliasMediator mediator;

    AliasCreateForm(final String id, final AliasMediator mediator) {
        super(id);
        this.mediator = mediator;
        final AliasMapping aliasMapping = new AliasMapping();
        setModel(aliasMapping);
        addAliasField(aliasMapping);
        addContentProviderDropDownChoice(aliasMapping);
        addCreateButton();
        addFeedbackPanel();
    }

    private void setModel(final AliasMapping aliasMapping) {
        final IModel<AliasMapping> formModel = new Model<>(aliasMapping);
        setModel(formModel);
    }

    private void addAliasField(final AliasMapping aliasMapping) {
        final AliasField aliasField = new AliasField("alias", aliasMapping);
        add(aliasField);
    }

    private void addContentProviderDropDownChoice(final AliasMapping aliasMapping) {
        final ContentProviderNameDropDownChoice contentProviderNameDropDownChoice = new ContentProviderNameDropDownChoice("contentProvider", aliasMapping);
        add(contentProviderNameDropDownChoice);
    }

    private void addCreateButton() {
        final AliasCreateButton createButton = new AliasCreateButton("createButton", mediator);
        add(createButton);
    }

    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        mediator.registerCreateFeedbackPanel(feedbackPanel);
        add(feedbackPanel);
    }

}
