package com.axiell.ehub;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public abstract class ConfirmationLink<L> extends Link<L> {

    public ConfirmationLink(final String id) {
        super(id);
        addConfirmation();
    }

    private String getConfirmationText() {
        StringResourceModel confirmationTextModel = new StringResourceModel("txtConfirmationText", this, new Model<>());
        return confirmationTextModel.getString();
    }

    private void addConfirmation() {
        final String confirmationText = getConfirmationText();
        final OnClickConfirmation confirmation = new OnClickConfirmation(confirmationText);
        add(confirmation);
    }
}
