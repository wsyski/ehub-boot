package com.axiell.ehub.consumer;

import com.axiell.ehub.config.DataSourceConfig;
import com.axiell.ehub.config.PersistenceConfig;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(classes =  {DataSourceConfig.class, PersistenceConfig.class, EhubConsumerRepositoryTest.TestConfig.class})
public class EhubConsumerHandlerTest {
    @Configuration
    @ComponentScan(basePackages = "com.axiell.ehub")
    public static class TestConfig {
    }

    private EhubConsumerHandler underTest;

    @Mock
    private IContentProviderAdminController contentProviderAdminController;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private EhubConsumer ehubConsumer;
    private List<ContentProvider> contentProviders;
    private boolean hasRemainingProviders;

    private List<ContentProvider> actualRemainingProviders;

    @BeforeEach
    public void setUpEhubConsumerHandler() {
        underTest = new EhubConsumerHandler();
    }

    @BeforeEach
    public void setUpContentProviderList() {
        contentProviders = Arrays.asList(contentProvider);
    }

    @Test
    public void hasRemainingProviders() {
        givenContentProviders();
        givenRemainingContentProviders();
        givenRetrieveRemainingContentProviders();
        whenHasRemainingProviders();
        thenHasRemainingProviders();
    }

    private void givenContentProviders() {
        given(contentProviderAdminController.getContentProviders()).willReturn(contentProviders);
    }

    @SuppressWarnings("unchecked")
    private void givenRemainingContentProviders() {
        given(ehubConsumer.getRemainingContentProviders(any(List.class))).willReturn(contentProviders);
    }

    private void givenRetrieveRemainingContentProviders() {
        underTest.retrieveRemainingContentProviders(contentProviderAdminController, ehubConsumer);
    }

    private void whenHasRemainingProviders() {
        hasRemainingProviders = underTest.hasRemainingProviders();
    }

    private void thenHasRemainingProviders() {
        Assertions.assertTrue(hasRemainingProviders);
    }

    @Test
    public void hasNoRemainingProviders() {
        givenContentProviders();
        givenNoRemainingContentProviders();
        givenRetrieveRemainingContentProviders();
        whenHasRemainingProviders();
        thenHasNoRemainingProviders();
    }

    @SuppressWarnings("unchecked")
    private void givenNoRemainingContentProviders() {
        given(ehubConsumer.getRemainingContentProviders(any(List.class))).willReturn(new ArrayList<ContentProvider>());
    }

    private void thenHasNoRemainingProviders() {
        Assertions.assertFalse(hasRemainingProviders);
    }

    @Test
    public void getRemainingProviders() {
        givenContentProviders();
        givenRemainingContentProviders();
        givenRetrieveRemainingContentProviders();
        whenGetRemainingProviders();
        thenActualRemainingProvidersIsNotEmpty();
    }

    void whenGetRemainingProviders() {
        actualRemainingProviders = underTest.getRemainingContentProviders();
    }

    void thenActualRemainingProvidersIsNotEmpty() {
        Assertions.assertNotNull(actualRemainingProviders);
        Assertions.assertFalse(actualRemainingProviders.isEmpty());
    }

    @Test
    public void getNoRemainingProviders() {
        givenContentProviders();
        givenNoRemainingContentProviders();
        givenRetrieveRemainingContentProviders();
        whenGetRemainingProviders();
        thenActualRemainingProvidersIsEmpty();
    }

    private void thenActualRemainingProvidersIsEmpty() {
        Assertions.assertNotNull(actualRemainingProviders);
        Assertions.assertTrue(actualRemainingProviders.isEmpty());
    }
}
