package com.axiell.ehub.consumer;

import com.axiell.ehub.provider.ContentProvider;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class EhubConsumerTest {
    private static final String CONTENT_PROVIDER_TEST_EP="TEST_EP";
    private EhubConsumer underTest;

    @Mock
    private ContentProviderConsumer askewsConsumer;
    @Mock
    private ContentProviderConsumer overDriveConsumer;
    @Mock
    private ContentProviderConsumer epConsumer;
    @Mock
    private ContentProvider askewsProvider;
    @Mock
    private ContentProvider overDriveProvider;
    @Mock
    private ContentProvider epProvider;
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
        given(askewsProvider.getName()).willReturn(ContentProvider.CONTENT_PROVIDER_ASKEWS);
    }

    private void givenOverDriveContentProviderConsumer() {
        given(overDriveConsumer.getContentProvider()).willReturn(overDriveProvider);
        given(overDriveProvider.getName()).willReturn(ContentProvider.CONTENT_PROVIDER_OVERDRIVE);
    }

    private void givenPublitContentProviderConsumer() {
        given(epConsumer.getContentProvider()).willReturn(epProvider);
        given(epProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    private void givenContentProvderConsumers() {
        Set<ContentProviderConsumer> consumers = new HashSet<>();
        consumers.add(overDriveConsumer);
        consumers.add(epConsumer);
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
        Assert.assertEquals(epConsumer, consumer3);
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
