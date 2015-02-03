package com.axiell.ehub.v1.provider.record.format;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.Formats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class FormatsV1ConverterTest {
    public static final String DESCRIPTION = "description";
    public static final String ID = "id";
    public static final String NAME = "name";
    @Mock
    private Formats formats;
    private Set<Format> formatSet;
    @Mock
    private Format format;
    private Formats_v1 actualFormats_v1;
    private Format_v1 actualFormat_v1;

    @Test
    public void convert() {
        given(format.getDescription()).willReturn(DESCRIPTION);
        given(format.getId()).willReturn(ID);
        given(format.getName()).willReturn(NAME);
        formatSet = Sets.newSet(format);
        given(formats.getFormats()).willReturn(formatSet);
        whenConvert();
        thenActualDescriptionEqualsExpected();
        thenActualIdEqualsExpected();
        thenActualNameEqualsExpected();
    }

    private void whenConvert() {
        actualFormats_v1 = FormatsV1Converter.convert(formats);
        actualFormat_v1 = actualFormats_v1.asList().get(0);
    }

    private void thenActualDescriptionEqualsExpected() {
        assertEquals(DESCRIPTION, actualFormat_v1.getDescription());
    }

    private void thenActualIdEqualsExpected() {
        assertEquals(ID, actualFormat_v1.getId());
    }

    private void thenActualNameEqualsExpected() {
        assertEquals(NAME, actualFormat_v1.getName());
    }
}
