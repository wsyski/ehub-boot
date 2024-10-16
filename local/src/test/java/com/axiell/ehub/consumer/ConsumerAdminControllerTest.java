package com.axiell.ehub.consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerAdminControllerTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private IConsumerAdminController underTest;
    
    @Mock
    private IEhubConsumerRepository ehubConsumerRepository;
    @Mock
    private IContentProviderConsumerRepository contentProviderConsumerRepository;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private EhubConsumer ehubConsumer;
    private ContentProviderConsumer actualContentProviderConsumer;

    @Before
    public void setUpConsumerAdminController() {
	underTest = new ConsumerAdminController();
	ReflectionTestUtils.setField(underTest, "ehubConsumerRepository", ehubConsumerRepository);
	ReflectionTestUtils.setField(underTest, "contentProviderConsumerRepository", contentProviderConsumerRepository);
    }
    
    @Test
    public void addContentProviderConsumerToEhubConsumer() {
	givenEhubConsumer();
	givenContentProviderConsumer();
	givenContentProviderConsumers();
	whenAddContentProviderConsumerToEhubConsumer();
	thenActualContentProviderConsumerEqualsExpectedContentProviderConsumer();
    }

    private void givenEhubConsumer() {
	given(ehubConsumerRepository.findById(EHUB_CONSUMER_ID)).willReturn(Optional.ofNullable(ehubConsumer));
	given(underTest.save(ehubConsumer)).willReturn(ehubConsumer);
    }

    private void givenContentProviderConsumer() {
	given(contentProviderConsumerRepository.save(contentProviderConsumer)).willReturn(contentProviderConsumer);
    }
    
    private void givenContentProviderConsumers() {	
	Set<ContentProviderConsumer> contentProviderConsumers = new HashSet<>();
	contentProviderConsumers.add(contentProviderConsumer);
	given(ehubConsumer.getContentProviderConsumers()).willReturn(contentProviderConsumers);
    }
    
    private void whenAddContentProviderConsumerToEhubConsumer() {
	actualContentProviderConsumer = underTest.add(EHUB_CONSUMER_ID, contentProviderConsumer);
    }
    
    private void thenActualContentProviderConsumerEqualsExpectedContentProviderConsumer() {
	Assert.assertNotNull(actualContentProviderConsumer);	
	Assert.assertEquals(contentProviderConsumer, actualContentProviderConsumer);
    }
}
