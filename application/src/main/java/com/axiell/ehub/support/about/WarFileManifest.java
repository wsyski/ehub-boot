package com.axiell.ehub.support.about;

import com.jcabi.manifests.Manifests;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public class WarFileManifest implements Serializable {
    private static final String BUILD_DATE = "Build-Date";
    private static final String BUILD_TIME = "Build-Time";
    private static final String IMPLEMENTATION_BUILD = "Implementation-Build";
    private static final String IMPLEMENTATION_VERSION = "Implementation-Version";

    private final String implementationBuild;
    private final String implementationVersion;
    private final String buildTime;

    public WarFileManifest() {
        this.implementationBuild = read(IMPLEMENTATION_BUILD);
        this.implementationVersion = read(IMPLEMENTATION_VERSION);
        this.buildTime = read(BUILD_TIME);
    }

    private String read(String name) {
        if (Manifests.exists(name))
            return Manifests.read(name);
        else {
            return StringUtils.EMPTY;
        }
    }
}
