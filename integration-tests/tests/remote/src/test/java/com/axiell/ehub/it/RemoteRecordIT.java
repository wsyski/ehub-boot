package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestDataConstants;
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
        givenExpectedEhubException(ErrorCause.EHUB_CONSUMER_NOT_FOUND
                .toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, String.valueOf(INVALID_EHUB_CONSUMER_ID))));
        whenGetRecord(invalidAuthInfo);
    }

    private void givenContentProviderGetFormatsResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/records/" + TestDataConstants.TEST_EP_RECORD_0_ID))
                .willReturn(aResponse().withBodyFile("getRecordResponse.json").withHeader("Content-Type", "application/json").withStatus(200)));
    }

    private void thenActualFormatsContainsExpectedComponents() {
        Assert.assertNotNull(record);
        List<Format> formats = record.formats();
        Assert.assertNotNull(formats);
        Assert.assertFalse(formats.isEmpty());
        for (Format format : formats) {
            thenFormatContainsExpectedComponents(format);
        }
    }

    private void thenFormatContainsExpectedComponents(Format format) {
        String id = format.id();
        Assert.assertNotNull(id);
        String name = format.name();
        Assert.assertNotNull(name);
        Set<String> platforms = format.platforms();
        Assert.assertTrue(platforms.size() > 0);
    }

    private void whenGetRecord(final AuthInfo authInfo) throws EhubException {
        record = underTest.getRecord(authInfo, CONTENT_PROVIDER_ALIAS, TestDataConstants.TEST_EP_RECORD_0_ID, LANGUAGE);
    }

    private AuthInfo givenInvalidAuthInfo() throws EhubException {
        expectedException.expect(EhubException.class);
        return new AuthInfo.Builder(INVALID_EHUB_CONSUMER_ID, TestDataConstants.EHUB_CONSUMER_SECRET_KEY).build();
    }

    protected boolean isLoanPerProduct() {
        return false;
    }
}
