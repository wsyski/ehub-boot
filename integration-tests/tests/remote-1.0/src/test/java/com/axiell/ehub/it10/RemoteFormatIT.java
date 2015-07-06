package com.axiell.ehub.it10;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.test.TestDataConstants;

public class RemoteFormatIT extends RemoteFormatITFixture {
    private IEhubService ehubService;

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void whenGetFormats() throws EhubException {
        actualFormats = ehubService.getFormats(authInfo, CONTENT_PROVIDER_ALIAS,  TestDataConstants.TEST_EP_RECORD_0_ID, LANGUAGE);
    }
}
