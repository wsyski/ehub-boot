package com.axiell.ehub.common.provider.record.format;

import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
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


    @BeforeEach
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
        Assertions.assertNull(actualTextBundle);
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
        Assertions.assertNotNull(actualTextBundle);
    }

    private void thenActualTextBundleEqualsSwedishTextBundle() {
        Assertions.assertEquals(swedishTextBundle, actualTextBundle);
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
        Assertions.assertEquals(defaultTextBundle, actualTextBundle);
    }
}
