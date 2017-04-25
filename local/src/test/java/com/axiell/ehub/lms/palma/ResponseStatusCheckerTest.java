package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.auth.Patron;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ResponseStatusCheckerTest {
    private ResponseStatusChecker underTest;
    @Mock
    private Status status;
    @Mock
    private EhubConsumer ehubConsumer;
    @Mock
    private Patron patron;
    @Mock
    private Patron.Builder patronBuilder;

    @Before
    public void setUpUnderTest() {
        underTest = new ResponseStatusChecker();
    }

    @Before
    public void setUpPatronBuilder() {
        given(patronBuilder.build()).willReturn(patron);
    }

    @Test
    public void checkResponseStatus_ok() {
        givenStatusOk();
        whenCheckResponseStatus();
    }

    private void whenCheckResponseStatus() {
        underTest.checkResponseStatus(status, ehubConsumer, patron);
    }

    private void givenStatusOk() {
        given(status.getType()).willReturn("ok");
    }
}
