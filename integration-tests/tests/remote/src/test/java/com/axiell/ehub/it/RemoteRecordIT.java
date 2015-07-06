package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteRecordIT extends RemoteITFixture {
    private Record record;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public final void getRecord() throws EhubException {
        givenContentProviderGetFormatsResponse();
        whenGetRecord(authInfo);
        thenActualFormatsContainsExpectedComponents();
    }

    @Test
    public void unauthorized() throws EhubException {
        AuthInfo invalidAuthInfo = givenInvalidAuthInfo();
        whenGetRecord(invalidAuthInfo);
        thenExpectedExceptionMessage();
    }

    private void thenExpectedExceptionMessage() {
        expectedException.expectMessage("An eHUB Consumer with ID 0 could not be found");
    }

    private void givenContentProviderGetFormatsResponse() {
        stubFor(get(urlEqualTo("/ep/api/v1/records/" + TestDataConstants.TEST_EP_RECORD_0_ID + "/formats"))
                .willReturn(aResponse().withBodyFile("getFormatsResponse.json").withHeader("Content-Type", "application/json").withStatus(200)));
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
    }

    private void whenGetRecord(final AuthInfo authInfo) throws EhubException {
        record = underTest.getRecord(authInfo, CONTENT_PROVIDER_ALIAS, TestDataConstants.TEST_EP_RECORD_0_ID, LANGUAGE);
    }

    private AuthInfo givenInvalidAuthInfo() throws EhubException {
        expectedException.expect(EhubException.class);
        return new AuthInfo.Builder(0L, "invalidSecret").build();
    }
}
