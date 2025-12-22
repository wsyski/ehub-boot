package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class AbstractContentProviderIT {
    protected String LANGUAGE="en";
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;
    @Mock
    protected EhubConsumer ehubConsumer;

    @BeforeTestClass
    public static void setUpWheteherIntegrationTestShouldBeRun() {
        boolean online = isOnline();
        Assumptions.assumeTrue(online);
    }

    private static boolean isOnline() {
        final String onlineProperty = System.getProperty("online");
        return onlineProperty == null ? false : Boolean.valueOf(onlineProperty);
    }

    protected void givenContentProvider() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    protected void givenEhubConsumer() {
        given(contentProviderConsumer.getEhubConsumer()).willReturn(ehubConsumer);
    }

    protected ApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"com/axiell/ehub/business-controller-context.xml"});
    }
}
