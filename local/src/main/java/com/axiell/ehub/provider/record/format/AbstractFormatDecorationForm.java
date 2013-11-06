package com.axiell.ehub.provider.record.format;

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
	addDispositionChoice();
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

    private void addDispositionChoice() {
	final ContentDispositionDropDownChoice dispositionChoice = new ContentDispositionDropDownChoice("contentDisposition", formModel, mediator);
	add(dispositionChoice);
    }

    private void addFormatDecorationSaveButton() {
	final AbstractFormatDecorationSaveButton<M> saveButton = makeSaveButton("submit");
	add(saveButton);
    }

    protected abstract AbstractFormatDecorationSaveButton<M> makeSaveButton(final String id);
}
