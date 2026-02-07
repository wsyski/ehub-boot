package com.axiell.ehub.provider.record.format;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

final class ContentDispositionDropDownChoice extends DropDownChoice<ContentDisposition> {
    private final IModel<FormatDecoration> formModel;

    ContentDispositionDropDownChoice(final String id, final IModel<FormatDecoration> formModel, final IContentDispositionChangedAwareMediator mediator) {
	super(id);
	this.formModel = formModel;
	setModel();
	setChoices();
	setChoiceRenderer();
	addOnChangeBehavior(mediator);
    }

    private void setModel() {
	final ContentDispositionModel dispositionModel = new ContentDispositionModel(formModel);
	setModel(dispositionModel);
    }

    private void setChoices() {
	final ContentDispositionsModel dispositionsModel = new ContentDispositionsModel();
	setChoices(dispositionsModel);
    }

    private void setChoiceRenderer() {
	final ContentDispositionChoiceRenderer dispositionChoiceRenderer = new ContentDispositionChoiceRenderer();
	setChoiceRenderer(dispositionChoiceRenderer);
    }

    private void addOnChangeBehavior(final IContentDispositionChangedAwareMediator mediator) {
	ContentDispositionChoiceOnChangeBehavior dispositionChoiceOnChangeBehavior = new ContentDispositionChoiceOnChangeBehavior(mediator);
	add(dispositionChoiceOnChangeBehavior);
    }

    private class ContentDispositionChoiceRenderer extends ChoiceRenderer<ContentDisposition> {

	@Override
	public String getIdValue(ContentDisposition object, int index) {
	    return object.toString();
	}

	@Override
	public Object getDisplayValue(ContentDisposition object) {
	    final StringResourceModel resourceModel = new StringResourceModel(object.toString(), ContentDispositionDropDownChoice.this, new Model<>());
	    return resourceModel.getString();
	}
    }
}
