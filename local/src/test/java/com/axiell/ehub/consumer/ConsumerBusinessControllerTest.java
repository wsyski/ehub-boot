package com.axiell.ehub.consumer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerBusinessControllerTest {
    private IConsumerBusinessController consumerBusinessController;    
    @Mock
    private IEhubConsumerRepository ehubConsumerRepository;
    @Mock
    private EhubConsumer expectedEhubConsumer;
    
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
	given(ehubConsumerRepository.findOne(any(Long.class))).willReturn(expectedEhubConsumer);
    }
    
    private void whenGetEhubConsumer() {
	consumerBusinessController.getEhubConsumer(1L);
    }
    
    private void theEhubConsumerIsFoundInDatabase() {
	InOrder inOrder = inOrder(ehubConsumerRepository);
	inOrder.verify(ehubConsumerRepository).findOne(any(Long.class));	
    }
    
    @Test
    public void ehubConsumerNotFound() {	
	givenEhubConsumerIsNotFoundInDatabase();
	try {
	    whenGetEhubConsumer();	
	} catch (UnauthorizedException e) {
	    thenActualErrorCauseEqualsExpectedErrorCause(e, ErrorCause.EHUB_CONSUMER_NOT_FOUND);
	}
    }

    private void givenEhubConsumerIsNotFoundInDatabase() {
	given(ehubConsumerRepository.findOne(any(Long.class))).willReturn(null);
    }
    
    private void thenActualErrorCauseEqualsExpectedErrorCause(UnauthorizedException e, ErrorCause expectedErrorCause) {
	ErrorCause actualErrorCause = getActualErrorCause(e);
	Assert.assertEquals(expectedErrorCause, actualErrorCause);
    }
    
    private ErrorCause getActualErrorCause(UnauthorizedException e) {
	EhubError ehubError = e.getEhubError();
	return ehubError.getCause();
    }
}
