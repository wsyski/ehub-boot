package com.axiell.ehub.consumer;

import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;

@RunWith(MockitoJUnitRunner.class)
public class EhubConsumerTest {
    private EhubConsumer underTest;
    
    @Mock
    private ContentProviderConsumer askewsConsumer;
    @Mock
    private ContentProviderConsumer overDriveConsumer;
    @Mock
    private ContentProviderConsumer publitConsumer;
    @Mock
    private ContentProvider askewsProvider;    
    @Mock
    private ContentProvider overDriveProvider;    
    @Mock
    private ContentProvider publitProvider;
    private List<ContentProviderConsumer> consumerList;
    
    @Before
    public void setUp() {
	underTest = new EhubConsumer();
    }

    @Test
    public void getContentProviderConsumersAsList() {	
	givenAskewsContentProviderConsumer();
	givenOverDriveContentProviderConsumer();
	givenPublitContentProviderConsumer();
	givenContentProvderConsumers();
	whenContentProviderConsumersAsList();	
	thenFirstConsumerEqualsAskewsConsumer();	
	thenSecondConsumerEqualsOverDriveConsumer();	
	thenThirdConsumerEqualsPublitConsumer();
    }

    private void givenAskewsContentProviderConsumer() {
	given(askewsConsumer.getContentProvider()).willReturn(askewsProvider);
	given(askewsProvider.getName()).willReturn(ContentProviderName.ASKEWS);
    }
    
    private void givenOverDriveContentProviderConsumer() {
	given(overDriveConsumer.getContentProvider()).willReturn(overDriveProvider);
	given(overDriveProvider.getName()).willReturn(ContentProviderName.OVERDRIVE);
    }
    
    private void givenPublitContentProviderConsumer() {
	given(publitConsumer.getContentProvider()).willReturn(publitProvider);
	given(publitProvider.getName()).willReturn(ContentProviderName.PUBLIT);
    }
    
    private void givenContentProvderConsumers() {
	Set<ContentProviderConsumer> consumers = new HashSet<>();
	consumers.add(overDriveConsumer);
	consumers.add(publitConsumer);
	consumers.add(askewsConsumer);
	underTest.setContentProviderConsumers(consumers);
    }
    
    private void whenContentProviderConsumersAsList() {
	consumerList = underTest.getContentProviderConsumersAsList();
    }
    
    private void thenFirstConsumerEqualsAskewsConsumer() {
	ContentProviderConsumer consumer1 = consumerList.get(0);
	Assert.assertEquals(askewsConsumer, consumer1);
    }

    private void thenSecondConsumerEqualsOverDriveConsumer() {
	ContentProviderConsumer consumer2 = consumerList.get(1);
	Assert.assertEquals(overDriveConsumer, consumer2);
    }
    
    private void thenThirdConsumerEqualsPublitConsumer() {
	ContentProviderConsumer consumer3 = consumerList.get(2);
	Assert.assertEquals(publitConsumer, consumer3);
    }
    
    @Test
    public void getContentProviderConsumersAsListWhenNoContentProviderConsumers() {
	whenContentProviderConsumersAsList();
	thenConsumerListIsNotNull();
	thenConsumerListIsEmpty();
    }

    private void thenConsumerListIsNotNull() {
	Assert.assertNotNull(consumerList);
    }
    
    private void thenConsumerListIsEmpty() {
	Assert.assertTrue(consumerList.isEmpty());
    }
}
