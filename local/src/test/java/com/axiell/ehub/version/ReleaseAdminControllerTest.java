package com.axiell.ehub.version;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ReleaseAdminControllerTest {
    private IReleaseAdminController underTest;
    
    @Mock
    private IReleaseRepository releaseRepository;
    private Release release1;
    private Release release2;
    private Release release3;
    private Release release4;
    private Release actualRelease;

    @Before
    public void setUp() {
	underTest = new ReleaseAdminController();
	ReflectionTestUtils.setField(underTest, "releaseRepository", releaseRepository);
    }
    
    @Test
    public void latestDatabaseRelease() {
	givenReleases();
	try {
	    whenGetLatestDatabaseRelease();
	} catch (EmptyReleaseTableException e) {
	    fail("An EmptyReleaseTableException should not have been thrown");
	}
	thenActualReleaseEqualsExpectedRelease();
    }

    private void whenGetLatestDatabaseRelease() throws EmptyReleaseTableException {
	actualRelease = underTest.getLatestDatabaseRelease();
    }

    private void givenReleases() {
	release1 = new Release();
	release1.setVersion("0.7");
	
	release2 = new Release();
	release2.setVersion("1.11");
	
	release3 = new Release();
	release3.setVersion("1.1");
	
	release4 = new Release();
	release4.setVersion("1.10");
	
	Set<Release> releases = new HashSet<>();
	releases.add(release1);
	releases.add(release2);
	releases.add(release3);
	releases.add(release4);	
	
	given(releaseRepository.findAll()).willReturn(releases);
    }
    
    private void thenActualReleaseEqualsExpectedRelease() {
	Assert.assertEquals(release2, actualRelease);
    }
    
    @Test
    public void testEmptyReleaseTableException() {
	givenNoReleases();
	try {
	    whenGetLatestDatabaseRelease();
	    fail("An EmptyReleaseTableException should have been thrown");
	} catch (EmptyReleaseTableException e) {
	    Assert.assertNotNull(e);
	}
    }

    private void givenNoReleases() {
	Set<Release> releases = new HashSet<>();
	given(releaseRepository.findAll()).willReturn(releases);
    }
}
