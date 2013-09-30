package com.axiell.ehub;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;

public class RemoteFormatIT_10Snapshot extends AbstractRemoteFormatIT {
    @Resource(name = "ehubClient")
    private IEhubService ehubService;

    @Override
    protected void setUpEhubClient(ApplicationContext springContext) {
	ehubService = (IEhubService) springContext.getBean("ehubClient");
    }

    @Override
    protected void whenGetFormats() throws EhubException {	
	actualFormats = ehubService.getFormats(authInfo, CONTENT_PROVIDER_NAME, DevelopmentData.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
