package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

abstract class AbstractFormatDecorationForm<M extends IContentDispositionChangedAwareMediator> extends StatelessForm<FormatDecoration> {
    protected final M mediator;
    protected final IModel<FormatDecoration> formModel;

    AbstractFormatDecorationForm(final String id, final M mediator) {
        super(id);
        this.mediator = mediator;
        formModel = new Model<>();
        setModel(formModel);

        addFormatIdField();
        addPlayerContainer();
        addDispositionField();
        addLockedField();
        addFormatDecorationSaveButton();
    }

    private void addFormatIdField() {
        final TextField<String> formatIdField = new TextField<>("contentProviderFormatId", new FormatIdModel(formModel));
        formatIdField.setVisible(isNewFormatDecoration());
        formatIdField.setRequired(isNewFormatDecoration());
        add(formatIdField);
    }

    protected abstract boolean isNewFormatDecoration();

    private void addPlayerContainer() {
        final PlayerContainer playerContainer = new PlayerContainer("playerContainer", formModel);
        mediator.registerPlayerContainer(playerContainer);
        add(playerContainer);
    }

    private void addLockedField() {
        final FormatDecorationIsLockedModel formatDecorationIsLockedModel = new FormatDecorationIsLockedModel(formModel);
        final CheckBox checkBox = new CheckBox("locked", formatDecorationIsLockedModel);
        checkBox.setOutputMarkupId(true);
        add(checkBox);
    }

    private void addDispositionField() {
        final ContentDispositionDropDownChoice dispositionChoice = new ContentDispositionDropDownChoice("contentDisposition", formModel, mediator);
        add(dispositionChoice);
    }

    private void addFormatDecorationSaveButton() {
        final AbstractFormatDecorationSaveButton<M> saveButton = makeSaveButton("submit");
        add(saveButton);
    }

    protected abstract AbstractFormatDecorationSaveButton<M> makeSaveButton(final String id);
}
