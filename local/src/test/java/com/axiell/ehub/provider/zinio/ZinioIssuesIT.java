package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.ErrorCauseArgumentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class ZinioIssuesIT extends AbstractZinioIT {
    private List<IssueDTO> issues;

    @Test
    public void getIssues() {
        givenConfigurationProperties();
        givenContentProvider();
        whenGetIssues(RECORD_ID);
        thenExpectedIssues();
    }

    @Test
    public void getIssuesWithInvalidRecordId() {
        givenConfigurationProperties();
        givenContentProvider();
        givenExpectedInternalServerException(ErrorCauseArgumentType.INVALID_CONTENT_PROVIDER_RECORD_ID.name());
        whenGetIssues(INVALID_RECORD_ID);
    }


    private void whenGetIssues(final String contentProviderRecordId) {
        issues = underTest.getIssues(contentProviderConsumer, contentProviderRecordId, Locale.ENGLISH.getLanguage());
    }

    private void thenExpectedIssues() {
        Assert.assertThat(issues, Matchers.notNullValue());
        Assert.assertThat(issues, Matchers.not(Matchers.empty()));
        Assert.assertThat(issues.get(0), Matchers.isA(IssueDTO.class));
    }
}