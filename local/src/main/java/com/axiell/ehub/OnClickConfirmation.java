package com.axiell.ehub;

import org.apache.wicket.AttributeModifier;

public class OnClickConfirmation extends AttributeModifier {

    public OnClickConfirmation(String confirmationText) {
	super("onclick", "return confirm('" + confirmationText + "');");
    }
}
