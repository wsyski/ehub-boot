package com.axiell.ehub.v1.provider.record;

import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.v1.provider.record.format.FormatsV1Converter;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

public final class RecordsResource_v1 implements IRecordsResource_v1 {
    private static final String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();
    private final IIssueBusinessController issueBusinessController;
    private final String contentProviderName;

    public RecordsResource_v1(final IIssueBusinessController issueBusinessController, final String contentProviderName) {
        this.issueBusinessController = issueBusinessController;
        this.contentProviderName = contentProviderName;
    }

    @Override
    public Formats_v1 getFormats(AuthInfo authInfo, String recordId, String language) {
        List<Issue> issues;
        if (StringUtils.isBlank(language)) {
            issues = issueBusinessController.getIssues(authInfo, contentProviderName, recordId, DEFAULT_LANGUAGE);
        } else {
            issues = issueBusinessController.getIssues(authInfo, contentProviderName, recordId, language);
        }
        return FormatsV1Converter.convert(issues);
    }
}
