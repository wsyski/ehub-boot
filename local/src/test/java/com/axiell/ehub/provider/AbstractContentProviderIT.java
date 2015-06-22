package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractContentProviderIT {
    protected String LANGUAGE="en";
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;

    @BeforeClass
    public static void setUpWheteherIntegrationTestShouldBeRun() {
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
