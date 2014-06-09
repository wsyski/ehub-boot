package com.axiell.ehub.provider.elib.library3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class Elib3CommandChainFactoryTest {
    private Elib3CommandChainFactory underTest;

    @Before
    public void setUpUnderTest() {
        underTest = new Elib3CommandChainFactory();
    }

    @Test
    public void createGetFormatsCommandChain() {
        GetFormatsCommandChain commandChain = underTest.createGetFormatsCommandChain();
        assertNotNull(commandChain);
    }

    @Test
    public void createCreateLoanCommandChain() {
        CreateLoanCommandChain commandChain = underTest.createCreateLoanCommandChain();
        assertNotNull(commandChain);
    }

    @Test
    public void createGetContentCommandChain() {
        GetContentCommandChain commandChain = underTest.createGetContentCommandChain();
        assertNotNull(commandChain);
    }
}
