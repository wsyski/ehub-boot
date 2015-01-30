package com.axiell.ehub;

import com.axiell.ehub.test.TestDataConstants;
import org.junit.Before;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;

public class RemoteFormatIT_08 extends AbstractRemoteFormatIT {
    private IEhubService ehubService;

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void whenGetFormats() throws EhubException {
        actualFormats = ehubService.getFormats(authInfo, CONTENT_PROVIDER_NAME,  TestDataConstants.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
