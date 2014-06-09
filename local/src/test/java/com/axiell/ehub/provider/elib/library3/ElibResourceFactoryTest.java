package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ElibResourceFactoryTest {
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    private IElibResource elibResource;

    @Test
    public void create() {
        givenContentProviderFromContentProviderConsumer();
        givenApiBaseUrlPropertyFromContentProvider();
        whenCreate();
        thenElibResourceIsCreated();
        thenApiBaseUrlPropertyIsRetrievedFromContentProvider();
    }

    private void givenContentProviderFromContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    private void givenApiBaseUrlPropertyFromContentProvider() {
        given(contentProvider.getProperty(API_BASE_URL)).willReturn("baseUrl");
    }

    private void whenCreate() {
        elibResource = ElibResourceFactory.create(contentProviderConsumer);
    }

    private void thenElibResourceIsCreated() {
        Assert.assertNotNull(elibResource);
    }

    private void thenApiBaseUrlPropertyIsRetrievedFromContentProvider() {
        verify(contentProvider).getProperty(API_BASE_URL);
    }
}
