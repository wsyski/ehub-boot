package com.axiell.ehub.provider;

import static org.mockito.BDDMockito.given;

import org.junit.Assume;
import org.junit.BeforeClass;
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
    
    @BeforeClass
    public static final void setUpWheteherIntegrationTestShouldBeRun() {
	boolean online = isOnline();	
	Assume.assumeTrue(online);
    }
    
    private static boolean isOnline() {
	final String onlineProperty = System.getProperty("online");
        return onlineProperty == null ? false : Boolean.valueOf(onlineProperty);
    }
    
    protected void givenContentProvider() {
	given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }
}
