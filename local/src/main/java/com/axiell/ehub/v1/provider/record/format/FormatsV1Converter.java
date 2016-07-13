package com.axiell.ehub.v1.provider.record.format;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;

import java.util.ArrayList;
import java.util.List;

public class FormatsV1Converter {

    public static Formats_v1 convert(final List<Issue> issues) {
        Formats_v1 formats_v1 = new Formats_v1();
        List<Format> formats = issues.isEmpty() ? new ArrayList<>() : issues.get(0).getFormats();

        for (Format format : formats) {
            Format_v1 format_v1 = new Format_v1(format.getId(), format.getName(), format.getDescription(), null);
            formats_v1.addFormat(format_v1);
        }

        return formats_v1;
    }
}
