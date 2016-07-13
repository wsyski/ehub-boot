package com.axiell.ehub.v1.provider.record.format;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.provider.record.issue.IssueBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FormatsV1ConverterTest {
    private Issue issue = IssueBuilder.issue();
    private Format format = issue.getFormats().get(0);
    private Format_v1 actualFormat_v1;

    @Test
    public void convert() {
        whenConvert();
        thenActualDescriptionEqualsExpected();
        thenActualIdEqualsExpected();
        thenActualNameEqualsExpected();
    }

    private void whenConvert() {
        Formats_v1 actualFormats_v1 = FormatsV1Converter.convert(Collections.singletonList(issue));
        actualFormat_v1 = actualFormats_v1.asList().get(0);
    }

    private void thenActualDescriptionEqualsExpected() {
        assertEquals(format.getDescription(), actualFormat_v1.getDescription());
    }

    private void thenActualIdEqualsExpected() {
        assertEquals(format.getId(), actualFormat_v1.getId());
    }

    private void thenActualNameEqualsExpected() {
        assertEquals(format.getName(), actualFormat_v1.getName());
    }
}
