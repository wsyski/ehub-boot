package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FormatFactoryTest {
    private static final String FORMAT_ID = "FORMAT_ID";
    private static final String FORMAT_NAME = "FORMAT_NAME";
    private static final String FORMAT_DESC = "FORMAT_DESC";
    private static final String LANGUAGE = "en";
    private FormatFactory underTest;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private FormatTextBundle textBundle;
    private Format actualFormat;

    @Before
    public void setUpUnderTest() {
        underTest = new FormatFactory();
    }

    @Test
    public void create_noAvailableTexts() {
        whenCreate();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsFormatId();
        thenNameEqualsFormatId();
    }

    private void whenCreate() {
        actualFormat = underTest.create(contentProvider, FORMAT_ID, LANGUAGE);
    }

    @Test
    public void create_availableTexts() {
        givenNameAndDescriptionInFormatTextBundle();
        whenCreate();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsExpectedDescription();
        thenNameEqualsExpectedName();
    }

    private void givenNameAndDescriptionInFormatTextBundle() {
        given(textBundle.getName()).willReturn(FORMAT_NAME);
        given(textBundle.getDescription()).willReturn(FORMAT_DESC);
        given(formatDecoration.getTextBundle(any(String.class))).willReturn(textBundle);
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }

    private void thenDescriptionEqualsExpectedDescription() {
        assertThat(FORMAT_DESC, is(actualFormat.getDescription()));
    }

    private void thenNameEqualsExpectedName() {
        assertThat(FORMAT_NAME, is(actualFormat.getName()));
    }

    private void thenActualFormatIdEqualsExpectedFormatId() {
        assertThat(FORMAT_ID, is(actualFormat.getId()));
    }

    private void thenDescriptionEqualsFormatId() {
        assertThat(FORMAT_ID, is(actualFormat.getDescription()));
    }

    private void thenNameEqualsFormatId() {
        assertThat(FORMAT_ID, is(actualFormat.getName()));
    }
}
