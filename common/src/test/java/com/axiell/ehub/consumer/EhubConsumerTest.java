package com.axiell.ehub.consumer;

import com.axiell.ehub.provider.ContentProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class EhubConsumerTest {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private EhubConsumer underTest;

    @Mock
    private ContentProvider epProvider;
    @Mock
    private ContentProviderConsumer epConsumer;

    @Mock
    private ContentProvider overDriveProvider;
    @Mock
    private ContentProviderConsumer overDriveConsumer;

    private List<ContentProviderConsumer> consumerList;

    @BeforeEach
    public void setUp() {
        underTest = new EhubConsumer();
    }

    @Test
    public void getContentProviderConsumersAsList() {
        givenEpContentProviderConsumer();
        givenOverDriveContentProviderConsumer();
        givenContentProvderConsumers();
        whenContentProviderConsumersAsList();
        thenFirstConsumerEqualsOverDriveConsumer();
    }

    private void givenEpContentProviderConsumer() {
        given(epConsumer.getContentProvider()).willReturn(epProvider);
        given(epProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
    }


    private void givenOverDriveContentProviderConsumer() {
        given(overDriveConsumer.getContentProvider()).willReturn(overDriveProvider);
        given(overDriveProvider.getName()).willReturn(ContentProvider.CONTENT_PROVIDER_OVERDRIVE);
    }


    private void givenContentProvderConsumers() {
        Set<ContentProviderConsumer> consumers = new HashSet<>();
        consumers.add(overDriveConsumer);
        consumers.add(epConsumer);
        underTest.setContentProviderConsumers(consumers);
    }

    private void whenContentProviderConsumersAsList() {
        consumerList = underTest.getContentProviderConsumersAsList();
    }

    private void thenFirstConsumerEqualsOverDriveConsumer() {
        ContentProviderConsumer consumer0 = consumerList.get(0);
        Assertions.assertEquals(overDriveConsumer, consumer0);
    }


    @Test
    public void getContentProviderConsumersAsListWhenNoContentProviderConsumers() {
        whenContentProviderConsumersAsList();
        thenConsumerListIsNotNull();
        thenConsumerListIsEmpty();
    }

    private void thenConsumerListIsNotNull() {
        Assertions.assertNotNull(consumerList);
    }

    private void thenConsumerListIsEmpty() {
        Assertions.assertTrue(consumerList.isEmpty());
    }
}
