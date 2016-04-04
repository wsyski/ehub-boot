package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.markup.html.panel.Panel;

abstract class AbstractPlatformFormPanel extends Panel {

    AbstractPlatformFormPanel(final String panelId, final Platform platform, final PlatformsMediator mediator) {
        super(panelId);
        addPlatformForm(platform, mediator);
    }

    private void addPlatformForm(final Platform platform, final PlatformsMediator mediator) {
        final AbstractPlatformForm platformForm = makePlatformForm(platform, mediator);
        add(platformForm);
    }

    protected abstract AbstractPlatformForm makePlatformForm(final Platform platform, final PlatformsMediator mediator);
}