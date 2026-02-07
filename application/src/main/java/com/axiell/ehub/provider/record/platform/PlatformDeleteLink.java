package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.ConfirmationLink;
import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class PlatformDeleteLink extends ConfirmationLink<Void> {
    private final Platform platform;
    private final PlatformsMediator mediator;

    @SpringBean(name = "platformAdminController")
    private IPlatformAdminController platformAdminController;

    PlatformDeleteLink(final String id, final Platform platform, final PlatformsMediator mediator) {
        super(id);
        this.platform = platform;
        this.mediator = mediator;
    }

    @Override
    public void onClick() {
        platformAdminController.delete(platform);
        mediator.afterDeletePlatform();
    }
}