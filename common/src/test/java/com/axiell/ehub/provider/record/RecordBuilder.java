package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.issue.IssueBuilder;

import java.util.Collections;

public class RecordBuilder {
    public static final String RECORD_ID = "recordId";

    public static Record record() {
        return new Record(RECORD_ID, Collections.singletonList(IssueBuilder.issue()));
    }

    public static Record periodicalRecord() {
        return new Record(RECORD_ID, Collections.singletonList(IssueBuilder.periodicalIssue()));
    }
}
