package com.axiell.ehub.version;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jcabi.manifests.Manifests;

@RunWith(MockitoJUnitRunner.class)
public class WarFileManifestTest {    
    private static byte[] manifests;
    private WarFileManifest underTest;
    private String implementationVersion;
    private String buildTime;
    
    @Mock
    private ServletContext servletContext;       
    
    @Before
    public void takeSnapshot() {
	WarFileManifestTest.manifests = Manifests.snapshot();
    }

    @After
    public void revertSnapshot() {
	Manifests.revert(WarFileManifestTest.manifests);
    }
    
    private void whenGetBuildTime() throws ManifestException {
	setUpWarFileManifest();
	buildTime = underTest.getBuildTime();
    }
    private void setUpWarFileManifest() throws ManifestException {
	underTest = WarFileManifest.read(servletContext);
    }
    
    @Test
    public void getNoBuildTime() throws ManifestException {
	whenGetBuildTime();
	thenBuildTimeIsNull();
    }
    
    private void thenBuildTimeIsNull() {
	Assert.assertNull(buildTime);	
    }
    
    @Test
    public void getBuildTime() throws ManifestException {
	givenBuildTime();
	whenGetBuildTime();
	thenBuildTimeIsNotNull();
    }
    
    private void givenBuildTime() {
	Manifests.inject("Build-Time", "value");
    }
    
    private void thenBuildTimeIsNotNull() {
	Assert.assertNotNull(buildTime);
    }

    @Test
    public void getImplementationVersion() throws ManifestException {
	givenImplementationVersion();
	whenGetImplementationVersion();
	thenImplementationVersionIsNotNull();
    }

    private void givenImplementationVersion() {
	Manifests.inject("Implementation-Version", "value");
    }
    
    private void thenImplementationVersionIsNotNull() {
	Assert.assertNotNull(implementationVersion);
    }
    
    private void whenGetImplementationVersion() throws ManifestException {
	setUpWarFileManifest();
	implementationVersion = underTest.getImplementationVersion();
    }
}
