package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.ContentDisposition;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.HashSet;

import static com.axiell.ehub.common.provider.record.format.FormatBuilder.FORMAT_DESCRIPTION;
import static com.axiell.ehub.common.provider.record.format.FormatBuilder.FORMAT_ID;
import static com.axiell.ehub.common.provider.record.format.FormatBuilder.FORMAT_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FormatFactoryTest {
    private static final String LANGUAGE = "en";
    private static final String PLATFORM = "platform";
    private static final ContentDisposition CONTENT_DISPOSITION = ContentDisposition.STREAMING;
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

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new FormatFactory();
    }

    @BeforeEach
    public void setUpFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
        given(formatDecoration.getPlatformNames()).willReturn(Collections.singleton(PLATFORM));
    }

    @Test
    public void create_fromContentProviderAndFormatId_noAvailableTexts_noContentDisposition() {
        whenCreateFromContentProviderAndFormatId();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsFormatId();
        thenNameEqualsFormatId();
        thenContentDispositionIsNull();
        thenEmptyPlatforms();
    }

    private void whenCreateFromContentProviderAndFormatId() {
        actualFormat = underTest.create(contentProvider, FORMAT_ID, LANGUAGE);
    }

    @Test
    public void create_fromContentProviderAndFormatId_availableTexts_contentDisposition() {
        givenNameAndDescriptionInFormatTextBundle();
        givenContentDisposition();
        givenFormatDecoration();
        whenCreateFromContentProviderAndFormatId();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsExpectedDescription();
        thenNameEqualsExpectedName();
        thenContentDispositionEqualsExpectedContentDisposition();
    }

    private void givenNameAndDescriptionInFormatTextBundle() {
        given(textBundle.getName()).willReturn(FORMAT_NAME);
        given(textBundle.getDescription()).willReturn(FORMAT_DESCRIPTION);
        given(formatDecoration.getTextBundle(any(String.class))).willReturn(textBundle);
    }

    private void givenContentDisposition() {
        given(formatDecoration.getContentDisposition()).willReturn(CONTENT_DISPOSITION);
    }

    private void givenFormatDecoration() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }

    private void thenDescriptionEqualsExpectedDescription() {
        assertThat(actualFormat.getDescription(), is(FORMAT_DESCRIPTION));
    }

    private void thenNameEqualsExpectedName() {
        assertThat(actualFormat.getName(), is(FORMAT_NAME));
    }

    private void thenActualFormatIdEqualsExpectedFormatId() {
        assertThat(actualFormat.getId(), is(FORMAT_ID));
    }

    private void thenDescriptionEqualsFormatId() {
        assertThat(actualFormat.getDescription(), is(FORMAT_ID));
    }

    private void thenNameEqualsFormatId() {
        assertThat(actualFormat.getName(), is(FORMAT_ID));
    }

    private void thenContentDispositionEqualsExpectedContentDisposition() {
        assertThat(actualFormat.getContentDisposition(), is(CONTENT_DISPOSITION));
    }

    private void thenContentDispositionIsNull() {
        assertThat(actualFormat.getContentDisposition(), is(nullValue()));
    }

    private void thenExpectedPlatforms() {
        assertThat(actualFormat.getPlatforms(), is(Collections.singleton(PLATFORM)));
    }

    private void thenEmptyPlatforms() {
        assertThat(actualFormat.getPlatforms(), is(new HashSet<>()));
    }

    @Test
    public void create_fromFormatDecoration_availableTexts_contentDisposition() {
        givenNameAndDescriptionInFormatTextBundle();
        givenContentDisposition();
        whenCreateFromFormatDecoration();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsExpectedDescription();
        thenNameEqualsExpectedName();
        thenContentDispositionEqualsExpectedContentDisposition();
        thenExpectedPlatforms();
    }

    private void whenCreateFromFormatDecoration() {
        actualFormat = underTest.create(formatDecoration, LANGUAGE);
    }


    @Test
    public void create_fromFormatDecoration_noAvailableTexts_noContentDisposition() {
        whenCreateFromFormatDecoration();
        thenActualFormatIdEqualsExpectedFormatId();
        thenDescriptionEqualsFormatId();
        thenNameEqualsFormatId();
        thenContentDispositionIsNull();
    }
}
