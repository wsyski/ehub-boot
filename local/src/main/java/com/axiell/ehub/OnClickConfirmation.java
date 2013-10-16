package com.axiell.ehub;

import org.apache.wicket.behavior.SimpleAttributeModifier;

public class OnClickConfirmation extends SimpleAttributeModifier {

    public OnClickConfirmation(String confirmationText) {
	super("onclick", "return confirm('" + confirmationText + "');");
    }
}
