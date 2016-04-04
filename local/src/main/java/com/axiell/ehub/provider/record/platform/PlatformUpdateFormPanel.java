package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;

public class PlatformUpdateFormPanel extends AbstractPlatformFormPanel {

    public PlatformUpdateFormPanel(final String panelId, final Platform platform, final PlatformsMediator mediator) {
        super(panelId, platform, mediator);
    }

    @Override
    protected AbstractPlatformForm makePlatformForm(final Platform platform, final PlatformsMediator mediator) {
        return new PlatformUpdateForm(platform, mediator);
    }
}
