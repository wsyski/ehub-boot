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

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteRecordIT extends RemoteITFixture {
    private static final long INVALID_EHUB_CONSUMER_ID = 0L;

    private Record record;

    @Test
    public final void getRecord() throws EhubException {
        givenContentProviderGetFormatsResponse();
        whenGetRecord(authInfo);
        thenActualFormatsContainsExpectedComponents();
    }

    @Test
    public void unauthorized() throws EhubException {
        AuthInfo invalidAuthInfo = givenInvalidAuthInfo();
        givenExpectedEhubException(UnauthorizedException.class,ErrorCause.EHUB_CONSUMER_NOT_FOUND,
                new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, String.valueOf(INVALID_EHUB_CONSUMER_ID)));
        whenGetRecord(invalidAuthInfo);
    }

    private void givenContentProviderGetFormatsResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/records/" + TestDataConstants.RECORD_0_ID))
                .willReturn(aResponse().withBodyFile("getRecordResponse.json").withHeader("Content-Type", "application/json").withStatus(200)));
    }

    private void thenActualFormatsContainsExpectedComponents() {
        Assert.assertNotNull(record);
        List<Issue> issues = record.getIssues();
        Assert.assertNotNull(issues);
        Assert.assertThat(issues.size(), Matchers.is(1));
        List<Format> formats = issues.get(0).getFormats();
        Assert.assertNotNull(formats);
        Assert.assertFalse(formats.isEmpty());
        for (Format format : formats) {
            thenFormatContainsExpectedComponents(format);
        }
    }

    private void thenFormatContainsExpectedComponents(final Format format) {
        String id = format.getId();
        Assert.assertNotNull(id);
        String name = format.getName();
        Assert.assertNotNull(name);
        Set<String> platforms = format.getPlatforms();
        Assert.assertTrue(platforms.size() > 0);
    }

    private void whenGetRecord(final AuthInfo authInfo) throws EhubException {
        record = underTest.getRecord(authInfo, CONTENT_PROVIDER_ALIAS, TestDataConstants.RECORD_0_ID, LANGUAGE);
    }

    private AuthInfo givenInvalidAuthInfo() throws EhubException {
        expectedException.expect(EhubException.class);
        return new AuthInfo.Builder(INVALID_EHUB_CONSUMER_ID, TestDataConstants.EHUB_CONSUMER_SECRET_KEY).build();
    }

    protected boolean isLoanPerProduct() {
        return false;
    }
}
