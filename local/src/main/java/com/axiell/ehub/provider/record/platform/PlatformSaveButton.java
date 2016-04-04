package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class PlatformSaveButton extends Button {
    private final IModel<Platform> formModel;
    private PlatformsMediator mediator;

    @SpringBean(name = "platformAdminController")
    private IPlatformAdminController platformAdminController;

    PlatformSaveButton(final String id, final IModel<Platform> formModel, final PlatformsMediator mediator) {
        super(id);
        this.formModel = formModel;
        this.mediator = mediator;
    }

    @Override
    public void onSubmit() {
        final Platform platform = formModel.getObject();
        platformAdminController.save(platform);
        mediator.afterSavePlatform();
    }
}