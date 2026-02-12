package com.axiell.ehub.common.provider.record;

import com.axiell.ehub.common.provider.record.issue.IssueBuilder;

import java.util.Collections;

public class RecordBuilder {
    public static final String RECORD_ID = "recordId";

    public static com.axiell.ehub.common.provider.record.Record record() {
        return new com.axiell.ehub.common.provider.record.Record(RECORD_ID, Collections.singletonList(IssueBuilder.issue()));
    }

    public static com.axiell.ehub.common.provider.record.Record periodicalRecord() {
        return new Record(RECORD_ID, Collections.singletonList(IssueBuilder.periodicalIssue()));
    }
}
