package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
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
    private com.axiell.arena.services.palma.util.v267.status.Status status267;
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

    @Test
    public void check267ResponseStatus_ok() {
        given267StatusOk();
        whenCheck267ResponseStatus();
    }

    private void given267StatusOk() {
        given(status267.getType()).willReturn("ok");
    }

    private void whenCheck267ResponseStatus() {
        underTest.check267ResponseStatus(status267, ehubConsumer, patronBuilder);
    }

    @Test(expected = ForbiddenException.class)
    public void check267ResponseStatus_invalidPin() {
        givenInvalidPinCodeMessage();
        whenCheck267ResponseStatus();
    }

    private void givenInvalidPinCodeMessage() {
        given(status267.getMessage()).willReturn("InvalidPinCode");
    }

    @Test(expected = ForbiddenException.class)
    public void check267ResponseStatus_blockedCard() {
        givenBlockedCardMessage();
        whenCheck267ResponseStatus();
    }

    private void givenBlockedCardMessage() {
        given(status267.getMessage()).willReturn("BlockedBorrCard");
    }

    @Test(expected = ForbiddenException.class)
    public void check267ResponseStatus_cardNotFound() {
        givenCardNotFoundMessage();
        whenCheck267ResponseStatus();
    }

    private void givenCardNotFoundMessage() {
        given(status267.getMessage()).willReturn("BorrCardNotFound");
    }

    @Test(expected = ForbiddenException.class)
    public void check267ResponseStatus_invalidAccountCard() {
        givenInvalidAccountCardMessage();
        whenCheck267ResponseStatus();
    }

    private void givenInvalidAccountCardMessage() {
        given(status267.getMessage()).willReturn("InvalidAccountCard");
    }

    @Test(expected = ForbiddenException.class)
    public void check267ResponseStatus_invalidCard() {
        givenInvalidCardMessage();
        whenCheck267ResponseStatus();
    }

    private void givenInvalidCardMessage() {
        given(status267.getMessage()).willReturn("InvalidBorrCard");
    }

    @Test(expected = ForbiddenException.class)
    public void check267ResponseStatus_invalidPatron() {
        givenInvalidPatronMessage();
        whenCheck267ResponseStatus();
    }

    private void givenInvalidPatronMessage() {
        given(status267.getMessage()).willReturn("InvalidPatron");
    }

    @Test(expected = InternalServerErrorException.class)
    public void check267ResponseStatus_notOkAndNoMessage() {
        whenCheck267ResponseStatus();
    }
}
