package com.axiell.ehub.local.provider.record.platform;

import java.io.Serializable;

class PlatformsMediator implements Serializable {
    private PlatformsPanel platformsPanel;

    void registerPlatformsPanel(final PlatformsPanel platformsPanel) {
        this.platformsPanel = platformsPanel;
    }

    void afterDeletePlatform() {
        platformsPanel.activate(platformsPanel);
    }

    void afterSavePlatform() {
        platformsPanel.activate(platformsPanel);
    }
}
