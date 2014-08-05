package com.axiell.ehub.support.about;

import com.jcabi.manifests.Manifests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;

public class WarFileManifest {
    private final String implementationVersion;
    private final String buildTime;

    private WarFileManifest(final String implementationVersion, final String buildTime) {
        this.implementationVersion = implementationVersion;
        this.buildTime = buildTime;
    }

    String getImplementationVersion() {
        return implementationVersion;
    }

    String getBuildTime() {
        return buildTime;
    }

    static WarFileManifest read(final ServletContext servletContext) throws ManifestException {
        return new Builder(servletContext).implementationVersion().buildTime().build();
    }

    private static class Builder {
        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);
        private static final String BUILD_TIME = "Build-Time";
        private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
        private String implementationVersion;
        private String buildTime;

        private Builder(final ServletContext servletContext) throws ManifestException {
            try {
                Manifests.append(servletContext);
            } catch (IOException e) {
                throw new ManifestException(e);
            }
        }

        Builder implementationVersion() {
            this.implementationVersion = read(IMPLEMENTATION_VERSION);
            return this;
        }

        Builder buildTime() {
            this.buildTime = read(BUILD_TIME);
            return this;
        }

        private String read(String name) {
            if (Manifests.exists(name))
                return Manifests.read(name);
            else {
                LOGGER.warn("Could not find attribute with name '" + name + "' in Manifest");
                return null;
            }
        }

        WarFileManifest build() {
            return new WarFileManifest(implementationVersion, buildTime);
        }
    }
}
