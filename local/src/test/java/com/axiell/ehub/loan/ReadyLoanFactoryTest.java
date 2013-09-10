package com.axiell.ehub.loan;

import static org.mockito.BDDMockito.given;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReadyLoanFactoryTest {
    private IReadyLoanFactory underTest;
    
    @Mock
    private EhubLoan ehubLoan;    
    @Mock
    private ContentProviderLoan contentProviderLoan;    
    @Mock
    private IContent content;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    private ReadyLoan actualReadyLoan;
    
    @Before
    public void setUp() {
	underTest = new ReadyLoanFactory();
    }
    
    @Test
    public void createReadyLoanWithContentProviderLoan() {
	whenCreateReadyLoanWithContentProviderLoan();
	thenActualReadyLoanIsNotNull();
    }

    private void whenCreateReadyLoanWithContentProviderLoan() {
	actualReadyLoan = underTest.createReadyLoan(ehubLoan, contentProviderLoan);
    }

    private void thenActualReadyLoanIsNotNull() {
	Assert.assertNotNull(actualReadyLoan);
    }
    
    @Test
    public void createReadyLoanWithContent() {
	givenContentProviderLoanMetadataInEhubLoan();	
	whenCreateReadyLoanWithContent();	
	thenActualReadyLoanIsNotNull();
    }

    private void givenContentProviderLoanMetadataInEhubLoan() {
	given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
    }
    
    private void whenCreateReadyLoanWithContent() {
	actualReadyLoan = underTest.createReadyLoan(ehubLoan, content);
    }
}
