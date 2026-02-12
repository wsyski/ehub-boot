package com.axiell.ehub.local.it.provider;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class AbstractContentProviderIT {
    protected String LANGUAGE = "en";
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;
    @Mock
    protected EhubConsumer ehubConsumer;
    @Autowired(required = false)
    private ApplicationContext applicationContext;

    protected void givenContentProvider() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    protected void givenEhubConsumer() {
        given(contentProviderConsumer.getEhubConsumer()).willReturn(ehubConsumer);
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
