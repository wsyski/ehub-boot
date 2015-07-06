package com.axiell.ehub.it12;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestDataConstants;
import junit.framework.Assert;
import org.junit.Test;

public class RemoteFormatIT extends RemoteFormatITFixture {
    private IEhubService ehubService;
    private AuthInfo invalidAuthInfo;

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void whenGetFormats() throws EhubException {
        actualFormats = ehubService.getFormats(authInfo, CONTENT_PROVIDER_ALIAS,  TestDataConstants.TEST_EP_RECORD_0_ID, LANGUAGE);
    }

    @Test(expected = EhubException.class)
    public void unauthorized() throws EhubException{
        givenInvalidAuthInfo();
        whenGetFormatsWithInvalidAuthInfo();
    }

    private void givenInvalidAuthInfo() {
        try {
            invalidAuthInfo = new AuthInfo.Builder(0L, "invalidSecret").build();
        } catch (EhubException e) {
            Assert.fail("An EhubException should not be thrown when an invalid AuthInfo is built");
        }
    }

    private void whenGetFormatsWithInvalidAuthInfo() throws EhubException {
        ehubService.getFormats(invalidAuthInfo, CONTENT_PROVIDER_ALIAS,  TestDataConstants.TEST_EP_RECORD_0_ID, LANGUAGE);
    }
}
