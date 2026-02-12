package com.axiell.ehub.local.about.support;

import com.axiell.ehub.local.support.about.WarFileManifest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WarFileManifestTest {
    private WarFileManifest underTest = new WarFileManifest();


    @Test
    public void manifest() {
        Assertions.assertNotNull(underTest.getImplementationBuild());
        Assertions.assertNotNull(underTest.getImplementationVersion());
        Assertions.assertNotNull(underTest.getBuildTime());
    }
}
