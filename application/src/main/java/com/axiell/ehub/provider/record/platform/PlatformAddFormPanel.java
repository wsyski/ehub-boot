package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;

class PlatformAddFormPanel extends AbstractPlatformFormPanel {

    PlatformAddFormPanel(final String panelId, final Platform platform, final PlatformsMediator mediator) {
        super(panelId, platform, mediator);
    }

    @Override
    protected AbstractPlatformForm makePlatformForm(final Platform platform, final PlatformsMediator mediator) {
        return new PlatformAddForm(mediator);
    }

    void resetForm() {
        PlatformAddForm platformAddForm=(PlatformAddForm)get("formPlatform");
        platformAddForm.setModelObject(new Platform());
    }
}
