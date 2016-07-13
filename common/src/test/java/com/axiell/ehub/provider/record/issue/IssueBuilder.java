package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.provider.record.format.FormatBuilder;

import java.util.Collections;

public class IssueBuilder {
    public static final String ISSUE_ID = "issueId";
    public static final String ISSUE_TITLE = "issueTitle";
    public static final String ISSUE_IMAGE_URL = "issueImageUrl";

    public static Issue periodicalIssue() {
        return new Issue(ISSUE_ID, ISSUE_TITLE, ISSUE_IMAGE_URL, Collections.singletonList(FormatBuilder.streamingFormat()));
    }

    public static Issue issue() {
        return new Issue(Collections.singletonList(FormatBuilder.streamingFormat()));
    }
}
