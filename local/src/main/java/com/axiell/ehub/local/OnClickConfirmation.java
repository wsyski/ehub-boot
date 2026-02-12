package com.axiell.ehub.local;

import org.apache.wicket.AttributeModifier;

public class OnClickConfirmation extends AttributeModifier {

    public OnClickConfirmation(String confirmationText) {
        super("onclick", "return confirm('" + confirmationText + "');");
    }
}
