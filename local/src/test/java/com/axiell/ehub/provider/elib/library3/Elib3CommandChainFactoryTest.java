package com.axiell.ehub.provider.elib.library3;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Elib3CommandChainFactoryTest {
    private Elib3CommandChainFactory underTest;

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new Elib3CommandChainFactory();
    }

    @Test
    public void createGetFormatsCommandChain() {
        GetFormatsCommandChain commandChain = underTest.createGetFormatsCommandChain();
        Assertions.assertNotNull(commandChain);
    }

    @Test
    public void createCreateLoanCommandChain() {
        CreateLoanCommandChain commandChain = underTest.createCreateLoanCommandChain();
        Assertions.assertNotNull(commandChain);
    }

    @Test
    public void createGetContentCommandChain() {
        GetContentCommandChain commandChain = underTest.createGetContentCommandChain();
        Assertions.assertNotNull(commandChain);
    }
}
