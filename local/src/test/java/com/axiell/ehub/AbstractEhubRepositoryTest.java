package com.axiell.ehub;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractEhubRepositoryTest<D extends DevelopmentData> extends AbstractTransactionalJUnit4SpringContextTests {
    
    // Test data
    protected D developmentData;

    protected abstract D initDevelopmentData();

    @BeforeTransaction
    public final void beforeTransaction() throws Exception {        
        developmentData = initDevelopmentData();
        developmentData.init();
    }

    @AfterTransaction
    public final void afterTransaction() throws Exception {
        developmentData.destroy();
    }

    protected static boolean isOnline() {
        return System.getProperty("online") != null && Boolean.valueOf(System.getProperty("online"));
    }
}
