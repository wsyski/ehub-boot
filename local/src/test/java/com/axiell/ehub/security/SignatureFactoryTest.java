package com.axiell.ehub.security;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;

@RunWith(MockitoJUnitRunner.class)
public class SignatureFactoryTest {    
    private SignatureFactory underTest;
    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private EhubConsumer ehubConsumer;
    private Signature actualSignature;
    
    @Before
    public void setUpSignatureFactory() {
	underTest = new SignatureFactory();
	ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
    }
    
    @Before
    public void setUpEhubConsumer() {
	given(ehubConsumer.getSecretKey()).willReturn("secret");
    }

    @Test
    public void createExpectedSignature() {
	ehubConsumerIsFoundInDatabase();
	whenCreateExpectedSignature();
	thenActualSignatureIsNotNull();
    }
    
    private void ehubConsumerIsFoundInDatabase() {
	given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(ehubConsumer);
    }

    private void whenCreateExpectedSignature() {
	actualSignature = underTest.createExpectedSignature(1L, "libraryCard", "pin");
    }

    private void thenActualSignatureIsNotNull() {
	Assert.notNull(actualSignature);
    }
}
