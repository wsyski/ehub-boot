package com.axiell.ehub.logging;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResteasyProviderFactory.class)
public class TimeLoggingResteasyProviderFactoryTest {
    @Mock
    private ResteasyProviderFactory providerFactory;
    private ResteasyProviderFactory result;

    @Test
    public void getInstance() {
        mockStatic(ResteasyProviderFactory.class);
        givenThereIsADefaultProviderFactory();
        whenTimeLoggingProviderFactoryIsConstructed();
        thenProviderFactoryIsTheExpected();
        thenDefaultFactoryWasUsedAsExpected();
        thenTimeLoggingInterceptorWasRegistered();
    }

    private void thenTimeLoggingInterceptorWasRegistered() {
        verify(providerFactory).registerProvider(TimeLoggingExecutionInterceptor.class);
    }

    private void thenDefaultFactoryWasUsedAsExpected() {
        PowerMockito.verifyStatic(times(1));
        ResteasyProviderFactory.getInstance();
    }

    private void thenProviderFactoryIsTheExpected() {
        assertEquals(providerFactory, result);
    }

    private void whenTimeLoggingProviderFactoryIsConstructed() {
        result = TimeLoggingResteasyProviderFactory.getInstance();
    }

    private void givenThereIsADefaultProviderFactory() {
        given(ResteasyProviderFactory.getInstance()).willReturn(providerFactory);
    }
}

