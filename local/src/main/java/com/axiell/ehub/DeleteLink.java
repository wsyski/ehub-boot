package com.axiell.ehub;

import org.apache.wicket.markup.html.link.Link;

public abstract class DeleteLink<L> extends Link<L> {

    public DeleteLink(final String id, final String confirmationText) {
	super(id);
	addConfirmation(confirmationText);
    }
    
    private void addConfirmation(final String confirmationText) {
	final OnClickConfirmation confirmation = new OnClickConfirmation(confirmationText);
        add(confirmation);
    }
}
