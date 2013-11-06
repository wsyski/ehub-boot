package com.axiell.ehub.consumer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;

@RunWith(MockitoJUnitRunner.class)
public class EhubConsumerHandlerTest {
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
    
    @Before
    public void setUpEhubConsumerHandler() {
	underTest = new EhubConsumerHandler();
    }
    
    @Before
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
	Assert.assertTrue(hasRemainingProviders);
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
	Assert.assertFalse(hasRemainingProviders);
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
	Assert.assertNotNull(actualRemainingProviders);
	Assert.assertFalse(actualRemainingProviders.isEmpty());
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
	Assert.assertNotNull(actualRemainingProviders);
	Assert.assertTrue(actualRemainingProviders.isEmpty());
    }
}
