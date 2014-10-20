package com.axiell.ehub;

import org.junit.Before;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;

public class RemoteFormatIT_10 extends AbstractRemoteFormatIT {
    private IEhubService ehubService;

    @Before
    public void givenPatronInAuthenticatePatronResponse() {
        stubFor(get(urlEqualTo("/arena.pa.palma/v267/patron?wsdl")).willReturn(aResponse().withHeader("Content-Type", "application/xml").withBodyFile("patron.wsdl")));
        stubFor(post(urlEqualTo("/arena.pa.palma/v267/patron")).withRequestBody(containing(":authenticatePatron xmlns")).willReturn(aResponse().withBodyFile("AuthenticatePatronResponse.xml").withStatus(200)));
    }

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void whenGetFormats() throws EhubException {
        actualFormats = ehubService.getFormats(authInfo, CONTENT_PROVIDER_NAME, DevelopmentData.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
