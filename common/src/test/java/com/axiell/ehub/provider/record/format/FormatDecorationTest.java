package com.axiell.ehub.provider.record.format;

import static org.mockito.BDDMockito.given;

import java.util.Map;

import com.axiell.ehub.language.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormatDecorationTest {
    private static final Language LANGUAGE_SWEDISH = new Language("sv");
    private static final Language LANGUAGE_ENGLISH = new Language("en");
    private FormatDecoration underTest;    
    
    @Mock
    private Map<Language, FormatTextBundle> textBundles;
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
	actualTextBundle = underTest.getTextBundle(LANGUAGE_SWEDISH.getId());
    }

    private void thenActualTextBundleIsNull() {
	Assert.assertNull(actualTextBundle);
    }
    
    @Test
    public void getSpecificTextBundle() {
	givenTextBundles();
	givenSwedishTextBundle();
	whenGetTextBundle();
	thenActualTextBundleIsNotNull();
	thenActualTextBundleEqualsSwedishTextBundle();
    }

    private void givenTextBundles() {
	underTest.setTextBundles(textBundles);
    }

    private void givenSwedishTextBundle() {
	given(textBundles.get(LANGUAGE_SWEDISH)).willReturn(swedishTextBundle);
    }

    private void thenActualTextBundleIsNotNull() {
	Assert.assertNotNull(actualTextBundle);
    }
    
    private void thenActualTextBundleEqualsSwedishTextBundle() {
	Assert.assertEquals(swedishTextBundle, actualTextBundle);
    }
    
    @Test
    public void getDefaultTextBundle() {
	givenTextBundles();
	givenNoSwedishTextBundle();
	givenDefaultTextBundle();
	whenGetTextBundle();
	thenActualTextBundleIsNotNull();
	thenActualTextBundleEqualsDefaultTextBundle();
    }

    private void givenNoSwedishTextBundle() {
	given(textBundles.get(LANGUAGE_SWEDISH)).willReturn(null);
    }
    
    private void givenDefaultTextBundle() {
	given(textBundles.get(LANGUAGE_ENGLISH)).willReturn(defaultTextBundle);
	
    }

    private void thenActualTextBundleEqualsDefaultTextBundle() {
	Assert.assertEquals(defaultTextBundle, actualTextBundle);
    }
}
