package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.forlagett.api.F1Service;
import se.forlagett.api.F1ServiceSoap;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class F1ServiceSoap12FactoryTest {
    private F1ServiceSoap12Factory underTest;
    @Mock
    private F1ServiceFactory f1ServiceFactory;
    @Mock
    private F1Service f1Service;
    @Mock
    private F1ServiceSoap expectedF1ServiceSoap;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    private F1ServiceSoap actualF1ServiceSoap;

    @Before
    public void setUpUnderTest() {
        given(f1Service.getF1ServiceSoap12()).willReturn(expectedF1ServiceSoap);
        given(f1ServiceFactory.create(any(ContentProviderConsumer.class))).willReturn(f1Service);
        underTest = new F1ServiceSoap12Factory();
        ReflectionTestUtils.setField(underTest, "f1ServiceFactory", f1ServiceFactory);
    }

    @Test
    public void getInstance() {
        whenGetInstance();
        thenActualF1ServiceSoapEqualsExpectedF1ServiceSoap();
    }

    private void whenGetInstance() {
        actualF1ServiceSoap = underTest.getInstance(contentProviderConsumer);
    }

    private void thenActualF1ServiceSoapEqualsExpectedF1ServiceSoap() {
        Assert.assertEquals(expectedF1ServiceSoap, actualF1ServiceSoap);
    }
}
