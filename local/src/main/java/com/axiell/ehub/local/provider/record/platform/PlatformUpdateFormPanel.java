package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;

public class PlatformUpdateFormPanel extends AbstractPlatformFormPanel {

    public PlatformUpdateFormPanel(final String panelId, final Platform platform, final PlatformsMediator mediator) {
        super(panelId, platform, mediator);
    }

    @Override
    protected AbstractPlatformForm makePlatformForm(final Platform platform, final PlatformsMediator mediator) {
        return new PlatformUpdateForm(platform, mediator);
    }
}
