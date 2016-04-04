package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;

class PlatformAddForm extends AbstractPlatformForm {
    PlatformAddForm(final PlatformsMediator mediator) {
        super(new Platform(), mediator);
    }
}
