package com.axiell.ehub.v2.provider.record.format;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.provider.record.issue.IssueBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FormatDTOV2ConverterTest {
    private Issue issue = IssueBuilder.issue();
    private Format format = issue.getFormats().get(0);
    private FormatDTO_v2 actualFormatDTO_v2;

    @Test
    public void convert() {
        whenConvert();
        thenActualDescriptionEqualsExpected();
        thenActualIdEqualsExpected();
        thenActualNameEqualsExpected();
    }

    private void whenConvert() {
        List<FormatDTO_v2> actualFormatsDTO_v2 = FormatDTOV2Converter.convert(Collections.singletonList(issue));
        actualFormatDTO_v2 = actualFormatsDTO_v2.get(0);
    }

    private void thenActualDescriptionEqualsExpected() {
        assertEquals(format.getDescription(), actualFormatDTO_v2.getDescription());
    }

    private void thenActualIdEqualsExpected() {
        assertEquals(format.getId(), actualFormatDTO_v2.getId());
    }

    private void thenActualNameEqualsExpected() {
        assertEquals(format.getName(), actualFormatDTO_v2.getName());
    }
}
