package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;

class PlatformAddForm extends AbstractPlatformForm {
    PlatformAddForm(final PlatformsMediator mediator) {
        super(new Platform(), mediator);
    }
}
