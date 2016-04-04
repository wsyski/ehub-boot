package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.PropertyModel;

class PlatformUpdateForm extends AbstractPlatformForm {

    PlatformUpdateForm(final Platform platform, final PlatformsMediator mediator) {
        super(platform, mediator);
        addPlatformIdField();
    }

    private void addPlatformIdField() {
        final HiddenField<Long> platformIdField = new HiddenField<>("fldId", new PropertyModel<>(getModel(), "id"));
        platformIdField.setType(Long.class);
        add(platformIdField);
    }
}
