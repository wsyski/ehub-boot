package com.axiell.ehub;

import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.security.AuthInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class AbstractRemoteFormatIT extends AbstractRemoteIT {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected Record record;

    @Override
    protected void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey()).libraryCard(testData.getLibraryCard())
                .pin(testData.getPin()).build();
    }

    @Test
    public final void getFormats() throws EhubException {
        givenGetProductResponse();
        whenGetFormats();
        thenActualFormatsContainsExpectedComponents();
        thenCustomFormatsValidation();
    }

    private void givenGetProductResponse() {
        stubFor(post(urlEqualTo("/webservices/GetProduct.asmx/GetProduct")).willReturn(aResponse().withBodyFile("GetProductResponse.xml").withHeader(
                "Content-Type", "application/xml").withStatus(200)));
    }

    protected abstract void whenGetFormats() throws EhubException;

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

    protected void thenCustomFormatsValidation() {
    }
}
