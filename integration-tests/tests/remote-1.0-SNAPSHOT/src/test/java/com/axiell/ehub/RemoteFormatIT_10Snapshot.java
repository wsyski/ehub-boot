package com.axiell.ehub;

public class RemoteFormatIT_10Snapshot extends AbstractRemoteFormatIT {
    private IEhubService ehubService;

    @Override
    protected void castBeanToIEhubService(Object bean) {
        ehubService = (IEhubService) bean;
    }

    @Override
    protected void whenGetFormats() throws EhubException {
        actualFormats = ehubService.getFormats(authInfo, "Distributör: Elib", DevelopmentData.ELIB_RECORD_0_ID, LANGUAGE);
    }
}
