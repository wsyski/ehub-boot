package com.axiell.ehub.provider.record.format;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;

public class PlayerContainer extends WebMarkupContainer {
    private final IModel<FormatDecoration> formModel;

    PlayerContainer(final String id, final IModel<FormatDecoration> formModel) {
	super(id);
	setOutputMarkupPlaceholderTag(true);
	this.formModel = formModel;

	addPlayerWidthField();
	addPlayerHeightField();
    }

    private void addPlayerWidthField() {
	final RequiredTextField<Integer> playerWidthField = new RequiredTextField<>("playerWidth", new PlayerWidthModel(formModel), Integer.class);
	add(playerWidthField);
    }

    private void addPlayerHeightField() {
	final RequiredTextField<Integer> playerHeightField = new RequiredTextField<>("playerHeight", new PlayerHeightModel(formModel), Integer.class);
	add(playerHeightField);
    }

    @Override
    public boolean isVisible() {
	final FormatDecoration formatDecoration = formModel.getObject();
	final ContentDisposition contentDisposition = formatDecoration.getContentDisposition();
	return ContentDisposition.STREAMING.equals(contentDisposition);
    }
}
