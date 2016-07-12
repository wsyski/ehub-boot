package com.axiell.ehub.v2.provider.record.format;

import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormatDTOV2Converter {

    public static List<FormatDTO_v2> convert(final List<Issue> issues) {
        List<Format> formats = issues.isEmpty() ? new ArrayList<>() : issues.get(0).getFormats();
        return formats.stream().map(format -> FormatDTO_v2.fromDTO(format.toDTO())).collect(Collectors.toList());
    }
}
