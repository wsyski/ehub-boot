package com.axiell.ehub.provider.record.format;

import static org.mockito.BDDMockito.given;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormatDecorationTest {
    private static final String SWEDISH = "sv";
    private static final String ENGLISH = "en";
    private FormatDecoration underTest;    
    
    @Mock
    private Map<String, FormatTextBundle> textBundles;    
    @Mock
    private FormatTextBundle swedishTextBundle;
    @Mock
    private FormatTextBundle defaultTextBundle;
    
    private FormatTextBundle actualTextBundle;
    

    @Before
    public void setUp() {
	underTest = new FormatDecoration();
    }
    
    @Test
    public void getNoTextBundle() {
	whenGetTextBundle();
	thenActualTextBundleIsNull();
    }

    private void whenGetTextBundle() {
	actualTextBundle = underTest.getTextBundle(SWEDISH);
    }

    private void thenActualTextBundleIsNull() {
	Assert.assertNull(actualTextBundle);
    }
    
    @Test
    public void getSpecificTextBundle() {
	givenSwedishTextBundles();
	givenSwedishTextBundle();
	whenGetTextBundle();
	thenActualTextBundleIsNotNull();
	thenActualTextBundleEqualsSwedishTextBundle();
    }

    private void givenSwedishTextBundles() {
	underTest.setTextBundles(textBundles);
    }

    private void givenSwedishTextBundle() {
	given(textBundles.get(SWEDISH)).willReturn(swedishTextBundle);
    }

    private void thenActualTextBundleIsNotNull() {
	Assert.assertNotNull(actualTextBundle);
    }
    
    private void thenActualTextBundleEqualsSwedishTextBundle() {
	Assert.assertEquals(swedishTextBundle, actualTextBundle);
    }
    
    @Test
    public void getDefaultTextBundle() {
	givenSwedishTextBundles();
	givenDefaultTextBundle();
	whenGetTextBundle();
	thenActualTextBundleIsNotNull();
	thenActualTextBundleEqualsDefaultTextBundle();
    }

    private void givenDefaultTextBundle() {
	given(textBundles.get(ENGLISH)).willReturn(defaultTextBundle);
	
    }

    private void thenActualTextBundleEqualsDefaultTextBundle() {
	Assert.assertEquals(defaultTextBundle, actualTextBundle);
    }
}
