package com.axiell.ehub.provider;

import static org.mockito.BDDMockito.given;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.consumer.ContentProviderConsumer;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractContentProviderIT {
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;
    
    protected void givenContentProvider() {
	given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }
}
