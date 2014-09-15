package com.axiell.ehub.provider.f1;


import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.forlagett.api.F1Service;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class F1ServiceFactoryTest {
    private static final String INVALID_URL = "url";
    private static final String VALID_URL = "http://194.68.86.40/F1Service.asmx?WSDL";
    private F1ServiceFactory underTest;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    private String apiBaseUrl;
    private F1Service actualF1Service;

    @Before
    public void setUpUnderTest() {
        underTest = new F1ServiceFactory();
    }

    @Test(expected = InternalServerErrorException.class)
    public void create_invalidUrl() {
        givenInvalidUrl();
        givenApiBaseUrlContentProviderProperty();
        whenCreate();
        thenGetApiBaseUrlContentProviderPropertyIsInvoked();
    }

    @Test
    public void create_validUrl() {
        givenValidWsdlUrl();
        givenApiBaseUrlContentProviderProperty();
        whenCreate();
        thenActualF1ServiceIsNotNull();
        thenGetApiBaseUrlContentProviderPropertyIsInvoked();
    }

    private void givenValidWsdlUrl() {
        apiBaseUrl = VALID_URL;
    }

    private void thenActualF1ServiceIsNotNull() {
        assertNotNull(actualF1Service);
    }

    private void thenGetApiBaseUrlContentProviderPropertyIsInvoked() {
        verify(contentProvider, times(1)).getProperty(API_BASE_URL);
    }

    private void givenInvalidUrl() {
        apiBaseUrl = INVALID_URL;
    }

    private void givenApiBaseUrlContentProviderProperty() {
        given(contentProvider.getProperty(API_BASE_URL)).willReturn(apiBaseUrl);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    private void whenCreate() {
        actualF1Service = underTest.create(contentProviderConsumer);
    }
}
