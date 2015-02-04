package com.axiell.ehub;

import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RemoteFormatIT_SNAPSHOT extends AbstractRemoteIT {
    private AuthInfo invalidAuthInfo;
    private Record record;

    @Test
    public final void getFormats() throws EhubException {
        givenGetProductResponse();
        whenGetFormats();
        thenActualFormatsContainsExpectedComponents();
    }

    @Test(expected = EhubException.class)
    public void unauthorized() throws EhubException {
        givenInvalidAuthInfo();
        whenGetFormatsWithInvalidAuthInfo();
    }

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

    private void givenInvalidAuthInfo() {
        try {
            invalidAuthInfo = new AuthInfo.Builder(0L, "invalidSecret").build();
        } catch (EhubException e) {
            junit.framework.Assert.fail("An EhubException should not be thrown when an invalid AuthInfo is built");
        }
    }

    private void whenGetFormatsWithInvalidAuthInfo() throws EhubException {
        underTest.getRecord(invalidAuthInfo, "Distributör: Elib", TestDataConstants.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
