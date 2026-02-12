package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;
import com.axiell.ehub.local.ConfirmationLink;
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
