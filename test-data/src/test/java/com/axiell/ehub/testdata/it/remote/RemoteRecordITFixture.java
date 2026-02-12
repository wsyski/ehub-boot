package com.axiell.ehub.testdata.it.remote;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.EhubWebApplicationException;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.client.IEhubServiceClient;
import com.axiell.ehub.common.provider.record.Record;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.common.security.UnauthorizedException;
import com.axiell.ehub.testdata.TestDataConstants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class RemoteRecordITFixture extends RemoteITFixture {
    private static final long INVALID_EHUB_CONSUMER_ID = 0L;

    private Record record;

    @Test
    public final void getRecord() throws EhubWebApplicationException {
        givenContentProviderGetRecordResponse();
        whenGetRecord(authInfo);
        thenActualRecordContainsExpectedComponents();
    }

    @Test
    public void unauthorized() throws EhubWebApplicationException {
        AuthInfo invalidAuthInfo = givenInvalidAuthInfo();
        Exception exception = Assertions.assertThrows(EhubWebApplicationException.class, () -> {
            whenGetRecord(invalidAuthInfo);
        });
        thenExpectedEhubException(exception, UnauthorizedException.class, ErrorCause.EHUB_CONSUMER_NOT_FOUND,
                new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, String.valueOf(INVALID_EHUB_CONSUMER_ID)));

    }

    private void thenActualRecordContainsExpectedComponents() {
        Assertions.assertNotNull(record);
        List<Issue> issues = record.getIssues();
        Assertions.assertNotNull(issues);
        int expectedIssueCount = getExpectedIssueCount();
        assertThat(issues.size(), Matchers.is(expectedIssueCount));
        issues.forEach(issue -> {
            if (expectedIssueCount > 1) {
                assertThat(issue.getId(), Matchers.notNullValue());
                assertThat(issue.getTitle(), Matchers.notNullValue());
                assertThat(issue.getImageUrl(), Matchers.notNullValue());
            } else {
                assertThat(issue.getId(), Matchers.nullValue());
                assertThat(issue.getTitle(), Matchers.nullValue());
                assertThat(issue.getImageUrl(), Matchers.nullValue());
            }
            List<Format> formats = issue.getFormats();
            assertThat(formats, Matchers.notNullValue());
            assertThat(formats.size(), Matchers.is(getExpectedFormatCount()));
            for (Format format : formats) {
                thenFormatContainsExpectedComponents(format);
            }
        });
    }

    private void thenFormatContainsExpectedComponents(final Format format) {
        String id = format.getId();
        assertThat(id, Matchers.notNullValue());
        String name = format.getName();
        assertThat(name, Matchers.notNullValue());
        Set<String> platforms = format.getPlatforms();
        assertThat(platforms.size(), Matchers.is(3));
    }

    private void whenGetRecord(final AuthInfo authInfo) throws EhubWebApplicationException {
        IEhubServiceClient ehubServiceClient = getEhubServiceClient();
        record = ehubServiceClient.getRecord(authInfo, getContentProviderAlias(), TestDataConstants.RECORD_ID_0, LANGUAGE);
    }

    private AuthInfo givenInvalidAuthInfo() throws EhubWebApplicationException {
        return new AuthInfo.Builder().ehubConsumerId(INVALID_EHUB_CONSUMER_ID).patron(new Patron.Builder().build()).build();
    }

    protected abstract int getExpectedIssueCount();

    protected abstract int getExpectedFormatCount();
}
