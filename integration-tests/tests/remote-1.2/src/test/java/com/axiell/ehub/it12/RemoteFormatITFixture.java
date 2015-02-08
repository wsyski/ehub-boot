package com.axiell.ehub.it12;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class RemoteFormatITFixture extends RemoteITFixture {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected Formats actualFormats;

    @Override
    protected void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey()).patronId(testData.getPatronId()).libraryCard(testData.getLibraryCard()).pin(testData.getPin()).build();
    }

    @Test
    public final void getFormats() throws EhubException {
        givenGetProductResponse();
        whenGetFormats();
        thenActualFormatsContainsExpectedComponents();
        thenCustomFormatsValidation();
    }

    private void givenGetProductResponse() {
        stubFor(post(urlEqualTo("/webservices/GetProduct.asmx/GetProduct")).willReturn(aResponse().withBodyFile("GetProductResponse.xml").withHeader("Content-Type", "application/xml").withStatus(200)));
    }

    protected abstract void whenGetFormats() throws EhubException;

    private void thenActualFormatsContainsExpectedComponents() {
        Assert.assertNotNull(actualFormats);
        Set<Format> formatSet = actualFormats.getFormats();
        Assert.assertNotNull(formatSet);
        Assert.assertFalse(formatSet.isEmpty());

        for (Format format : formatSet) {
            thenFormatContainsExpectedComponents(format);
        }
    }

    private void thenFormatContainsExpectedComponents(Format format) {
        String id = format.getId();
        Assert.assertNotNull(id);
        String name = format.getName();
        Assert.assertNotNull(name);
    }

    protected void thenCustomFormatsValidation() {
    }
}
