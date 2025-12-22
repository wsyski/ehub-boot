package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        Assertions.assertNotNull(elibResource);
    }

    private void thenApiBaseUrlPropertyIsRetrievedFromContentProvider() {
        verify(contentProvider).getProperty(API_BASE_URL);
    }
}
