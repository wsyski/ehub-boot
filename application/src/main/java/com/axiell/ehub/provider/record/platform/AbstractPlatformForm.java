package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.PropertyModel;

class AbstractPlatformForm extends StatelessForm<Platform> {

    AbstractPlatformForm(final Platform platform, final PlatformsMediator mediator) {
        super("formPlatform");
        setModel(new PlatformModel(platform));
        addPlatformNameField();
        addSaveButton(mediator);
    }

    private void addPlatformNameField() {
        final RequiredTextField<String> platformNameField = new RequiredTextField<>("fldName", new PropertyModel<>(getDefaultModel(), "name"));
        platformNameField.setOutputMarkupId(true);
        add(platformNameField);
    }

    private void addSaveButton(final PlatformsMediator mediator) {
        final PlatformSaveButton saveButton = new PlatformSaveButton("btnSubmit", getModel(), mediator);
        add(saveButton);
    }
}