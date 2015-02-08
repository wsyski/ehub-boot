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

public class RemoteFormatIT extends RemoteITFixture {
    private AuthInfo invalidAuthInfo;
    private Record record;

    @Test
    public final void getFormats() throws EhubException {
        givenGetProductResponse();
        whenGetFormats();
        thenActualFormatsContainsExpectedComponents();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void unauthorized() throws EhubException {
        givenInvalidAuthInfo();
        thrown.expect(EhubException.class);
        thrown.expectMessage("An eHUB Consumer with ID 0 could not be found");
        whenGetFormatsWithInvalidAuthInfo();
    }

    @Rule
    public ExpectedException expectedExxeption = ExpectedException.none();

    private void givenGetProductResponse() {
        stubFor(post(urlEqualTo("/webservices/GetProduct.asmx/GetProduct")).willReturn(aResponse().withBodyFile("GetProductResponse.xml").withHeader(
                "Content-Type", "application/xml").withStatus(200)));
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

    private void whenGetFormats() throws EhubException {
        record = underTest.getRecord(authInfo, "Distribut\u00f6r: Elib", TestDataConstants.ELIB_RECORD_0_ID, LANGUAGE);
    }

    private void givenInvalidAuthInfo() throws EhubException {
        invalidAuthInfo = new AuthInfo.Builder(0L, "invalidSecret").build();
    }

    private void whenGetFormatsWithInvalidAuthInfo() throws EhubException {
        underTest.getRecord(invalidAuthInfo, "Distributör: Elib", TestDataConstants.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
