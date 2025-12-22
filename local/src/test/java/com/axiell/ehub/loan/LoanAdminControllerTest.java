package com.axiell.ehub.loan;

import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoanAdminControllerTest {
    private static final long LOANS = 10;
    private ILoanAdminController underTest;

    @Mock
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;
    @Mock
    private FormatDecoration formatDecoration;
    private long actualCount;

    @BeforeEach
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
        Assertions.assertEquals(LOANS, actualCount);
    }
}
