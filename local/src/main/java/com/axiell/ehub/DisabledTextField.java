package com.axiell.ehub;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

public class DisabledTextField extends RequiredTextField<String> {

    public DisabledTextField(String id, Object modelObject, String modelExpression) {
	super(id);
	final PropertyModel<String> model = new PropertyModel<>(modelObject, modelExpression);
	setModel(model);
        setEnabled(false);
    }
}
