package com.axiell.ehub.loan;

import static org.mockito.BDDMockito.given;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.provider.record.format.FormatDecoration;

@RunWith(MockitoJUnitRunner.class)
public class LoanAdminControllerTest {
    private static final long LOANS = 10;
    private ILoanAdminController underTest;
    
    @Mock
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;
    @Mock
    private FormatDecoration formatDecoration;    
    private long actualCount;

    @Before
    public void setUpLoanAdminController() {
	underTest = new LoanAdminController();
	ReflectionTestUtils.setField(underTest, "ehubLoanRepositoryFacade", ehubLoanRepositoryFacade);
    }
    
    @Test
    public void countLoansByFormatDecoration() {
	givenTenLoans();
	whenCountLoansByFormatDecoration();
	thenActualCountIsTen();
    }

    private void givenTenLoans() {
	given(ehubLoanRepositoryFacade.countLoansByFormatDecoration(formatDecoration)).willReturn(LOANS);
    }

    private void whenCountLoansByFormatDecoration() {
	actualCount = underTest.countLoansByFormatDecoration(formatDecoration);
    }

    private void thenActualCountIsTen() {
	Assert.assertEquals(LOANS, actualCount);
    }
}
