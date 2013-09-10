package com.axiell.ehub.consumer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerBusinessControllerTest {
    private IConsumerBusinessController consumerBusinessController;    
    @Mock
    private IEhubConsumerRepository ehubConsumerRepository;
    
    @Before
    public void setUpConsumerBusinessController() {
	consumerBusinessController = new ConsumerBusinessController();	
	ReflectionTestUtils.setField(consumerBusinessController, "ehubConsumerRepository", ehubConsumerRepository);
    }
    
    @Test
    public void getEhubConsumer() {
	givenEhubConsumerIsFoundInDatabase();	
	whenGetEhubConsumer();	
	theEhubConsumerIsFoundInDatabase();
    }

    private void givenEhubConsumerIsFoundInDatabase() {
	given(ehubConsumerRepository.findOne(any(Long.class))).willReturn(any(EhubConsumer.class));
    }
    
    private void whenGetEhubConsumer() {
	consumerBusinessController.getEhubConsumer(1L);
    }
    
    private void theEhubConsumerIsFoundInDatabase() {
	InOrder inOrder = inOrder(ehubConsumerRepository);
	inOrder.verify(ehubConsumerRepository).findOne(any(Long.class));	
    }
}
