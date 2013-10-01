package com.axiell.ehub;

public class RemoteFormatIT_04 extends AbstractRemoteFormatIT {
    private IEhubService ehubService;

    @Override
    protected void castBeanToIEhubService(Object bean) {
	ehubService = (IEhubService) bean;
    }

    @Override
    protected void whenGetFormats() throws EhubException {	
	actualFormats = ehubService.getFormats(authInfo, CONTENT_PROVIDER_NAME, DevelopmentData.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
