package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.issue.Issue;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.security.UnauthorizedException;
import com.axiell.ehub.test.TestDataConstants;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public abstract class RemoteRecordITFixture extends RemoteITFixture {
    private static final long INVALID_EHUB_CONSUMER_ID = 0L;

    private Record record;

    @Test
    public final void getRecord() throws EhubException {
        givenContentProviderGetRecordResponse();
        whenGetRecord(authInfo);
        thenActualRecordContainsExpectedComponents();
    }

    @Test
    public void unauthorized() throws EhubException {
        AuthInfo invalidAuthInfo = givenInvalidAuthInfo();
        givenExpectedEhubException(UnauthorizedException.class, ErrorCause.EHUB_CONSUMER_NOT_FOUND,
                new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, String.valueOf(INVALID_EHUB_CONSUMER_ID)));
        whenGetRecord(invalidAuthInfo);
    }

    private void thenActualRecordContainsExpectedComponents() {
        Assert.assertNotNull(record);
        List<Issue> issues = record.getIssues();
        Assert.assertNotNull(issues);
        int expectedIssueCount = getExpectedIssueCount();
        Assert.assertThat(issues.size(), Matchers.is(expectedIssueCount));
        issues.forEach(issue -> {
            if (expectedIssueCount > 1) {
                Assert.assertThat(issue.getId(), Matchers.notNullValue());
                Assert.assertThat(issue.getTitle(), Matchers.notNullValue());
                Assert.assertThat(issue.getImageUrl(), Matchers.notNullValue());
            } else {
                Assert.assertThat(issue.getId(), Matchers.nullValue());
                Assert.assertThat(issue.getTitle(), Matchers.nullValue());
                Assert.assertThat(issue.getImageUrl(), Matchers.nullValue());
            }
            List<Format> formats = issue.getFormats();
            Assert.assertThat(formats, Matchers.notNullValue());
            Assert.assertThat(formats.size(),Matchers.is(getExpectedFormatCount()));
            for (Format format : formats) {
                thenFormatContainsExpectedComponents(format);
            }
        });
    }

    private void thenFormatContainsExpectedComponents(final Format format) {
        String id = format.getId();
        Assert.assertThat(id, Matchers.notNullValue());
        String name = format.getName();
        Assert.assertThat(name, Matchers.notNullValue());
        Set<String> platforms = format.getPlatforms();
        Assert.assertThat(platforms.size(), Matchers.is(3));
    }

    private void whenGetRecord(final AuthInfo authInfo) throws EhubException {
        record = underTest.getRecord(authInfo, getContentProviderAlias(), TestDataConstants.RECORD_0_ID, LANGUAGE);
    }

    private AuthInfo givenInvalidAuthInfo() throws EhubException {
        expectedException.expect(EhubException.class);
        return new AuthInfo.Builder(INVALID_EHUB_CONSUMER_ID, TestDataConstants.EHUB_CONSUMER_SECRET_KEY).build();
    }

    protected abstract void givenContentProviderGetRecordResponse();

    protected abstract int getExpectedIssueCount();
    protected abstract int getExpectedFormatCount();
}
