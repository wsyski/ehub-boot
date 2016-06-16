package com.axiell.ehub.provider.ep.lpp.ulverscroft;

import com.axiell.ehub.provider.ep.lpp.AbstractLppEpIT;
import com.google.common.collect.Lists;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UlverscroftIT extends AbstractLppEpIT {
    private static final String API_BASE_URL_VALUE = "https://xyzaeh.ulverscroftdigital.com";
    private static final String EP_SITE_ID = "1111";
    private static final String EP_SECRET_KEY = "c2VjcmV0S2V5";
    private static final String RECORD_ID = "9781407941011";
    private static final String FORMAT_ID_0 = "mp3";
    private static final String FORMAT_ID_1 = "m4b";

    @Override
    protected List<String> getFormatIds() {
         return Lists.newArrayList(FORMAT_ID_0,FORMAT_ID_1);
    }

    @Override
    protected String getApiBaseUri() {
        return API_BASE_URL_VALUE;
    }

    @Override
    protected String getSiteId() {
        return EP_SITE_ID;
    }

    @Override
    protected String getSecretKey() {
        return EP_SECRET_KEY;
    }

    @Override
    protected String getRecordId() {
        return RECORD_ID;
    }

}
